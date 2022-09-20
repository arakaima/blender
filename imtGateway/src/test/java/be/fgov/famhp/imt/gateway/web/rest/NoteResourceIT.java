package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.Note;
import be.fgov.famhp.imt.gateway.repository.NoteRepository;
import be.fgov.famhp.imt.gateway.service.dto.NoteDTO;
import be.fgov.famhp.imt.gateway.service.mapper.NoteMapper;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link NoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class NoteResourceIT {

    private static final String ENTITY_API_URL = "/api/notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Note note;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createEntity() {
        Note note = new Note();
        return note;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createUpdatedEntity() {
        Note note = new Note();
        return note;
    }

    @BeforeEach
    public void initTest() {
        noteRepository.deleteAll().block();
        note = createEntity();
    }

    @Test
    void createNote() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().collectList().block().size();
        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate + 1);
        Note testNote = noteList.get(noteList.size() - 1);
    }

    @Test
    void createNoteWithExistingId() throws Exception {
        // Create the Note with an existing ID
        note.setId("existing_id");
        NoteDTO noteDTO = noteMapper.toDto(note);

        int databaseSizeBeforeCreate = noteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllNotesAsStream() {
        // Initialize the database
        noteRepository.save(note).block();

        List<Note> noteList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(NoteDTO.class)
            .getResponseBody()
            .map(noteMapper::toEntity)
            .filter(note::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(noteList).isNotNull();
        assertThat(noteList).hasSize(1);
        Note testNote = noteList.get(0);
    }

    @Test
    void getAllNotes() {
        // Initialize the database
        noteRepository.save(note).block();

        // Get all the noteList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(note.getId()));
    }

    @Test
    void getNote() {
        // Initialize the database
        noteRepository.save(note).block();

        // Get the note
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, note.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(note.getId()));
    }

    @Test
    void getNonExistingNote() {
        // Get the note
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingNote() throws Exception {
        // Initialize the database
        noteRepository.save(note).block();

        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();

        // Update the note
        Note updatedNote = noteRepository.findById(note.getId()).block();
        NoteDTO noteDTO = noteMapper.toDto(updatedNote);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, noteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        Note testNote = noteList.get(noteList.size() - 1);
    }

    @Test
    void putNonExistingNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();
        note.setId(UUID.randomUUID().toString());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, noteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();
        note.setId(UUID.randomUUID().toString());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();
        note.setId(UUID.randomUUID().toString());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNoteWithPatch() throws Exception {
        // Initialize the database
        noteRepository.save(note).block();

        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();

        // Update the note using partial update
        Note partialUpdatedNote = new Note();
        partialUpdatedNote.setId(note.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNote.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNote))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        Note testNote = noteList.get(noteList.size() - 1);
    }

    @Test
    void fullUpdateNoteWithPatch() throws Exception {
        // Initialize the database
        noteRepository.save(note).block();

        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();

        // Update the note using partial update
        Note partialUpdatedNote = new Note();
        partialUpdatedNote.setId(note.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNote.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNote))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        Note testNote = noteList.get(noteList.size() - 1);
    }

    @Test
    void patchNonExistingNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();
        note.setId(UUID.randomUUID().toString());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, noteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();
        note.setId(UUID.randomUUID().toString());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();
        note.setId(UUID.randomUUID().toString());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNote() {
        // Initialize the database
        noteRepository.save(note).block();

        int databaseSizeBeforeDelete = noteRepository.findAll().collectList().block().size();

        // Delete the note
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, note.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
