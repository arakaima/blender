import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AttachedDocumentFormService } from './attached-document-form.service';
import { AttachedDocumentService } from '../service/attached-document.service';
import { IAttachedDocument } from '../attached-document.model';

import { AttachedDocumentUpdateComponent } from './attached-document-update.component';

describe('AttachedDocument Management Update Component', () => {
  let comp: AttachedDocumentUpdateComponent;
  let fixture: ComponentFixture<AttachedDocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let attachedDocumentFormService: AttachedDocumentFormService;
  let attachedDocumentService: AttachedDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AttachedDocumentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AttachedDocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttachedDocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    attachedDocumentFormService = TestBed.inject(AttachedDocumentFormService);
    attachedDocumentService = TestBed.inject(AttachedDocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const attachedDocument: IAttachedDocument = { id: 'CBA' };

      activatedRoute.data = of({ attachedDocument });
      comp.ngOnInit();

      expect(comp.attachedDocument).toEqual(attachedDocument);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttachedDocument>>();
      const attachedDocument = { id: 'ABC' };
      jest.spyOn(attachedDocumentFormService, 'getAttachedDocument').mockReturnValue(attachedDocument);
      jest.spyOn(attachedDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attachedDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attachedDocument }));
      saveSubject.complete();

      // THEN
      expect(attachedDocumentFormService.getAttachedDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(attachedDocumentService.update).toHaveBeenCalledWith(expect.objectContaining(attachedDocument));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttachedDocument>>();
      const attachedDocument = { id: 'ABC' };
      jest.spyOn(attachedDocumentFormService, 'getAttachedDocument').mockReturnValue({ id: null });
      jest.spyOn(attachedDocumentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attachedDocument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attachedDocument }));
      saveSubject.complete();

      // THEN
      expect(attachedDocumentFormService.getAttachedDocument).toHaveBeenCalled();
      expect(attachedDocumentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttachedDocument>>();
      const attachedDocument = { id: 'ABC' };
      jest.spyOn(attachedDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attachedDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(attachedDocumentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
