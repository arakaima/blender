jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CapaDocumentService } from '../service/capa-document.service';

import { CapaDocumentDeleteDialogComponent } from './capa-document-delete-dialog.component';

describe('CapaDocument Management Delete Component', () => {
  let comp: CapaDocumentDeleteDialogComponent;
  let fixture: ComponentFixture<CapaDocumentDeleteDialogComponent>;
  let service: CapaDocumentService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CapaDocumentDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(CapaDocumentDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CapaDocumentDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CapaDocumentService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('ABC');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('ABC');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
