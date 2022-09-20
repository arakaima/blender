import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CapaDocumentDetailComponent } from './capa-document-detail.component';

describe('CapaDocument Management Detail Component', () => {
  let comp: CapaDocumentDetailComponent;
  let fixture: ComponentFixture<CapaDocumentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CapaDocumentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ capaDocument: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(CapaDocumentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CapaDocumentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load capaDocument on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.capaDocument).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
