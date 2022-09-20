import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AttachedDocumentDetailComponent } from './attached-document-detail.component';

describe('AttachedDocument Management Detail Component', () => {
  let comp: AttachedDocumentDetailComponent;
  let fixture: ComponentFixture<AttachedDocumentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AttachedDocumentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ attachedDocument: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(AttachedDocumentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AttachedDocumentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load attachedDocument on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.attachedDocument).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
