import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CapaDocumentFormService } from './capa-document-form.service';
import { CapaDocumentService } from '../service/capa-document.service';
import { ICapaDocument } from '../capa-document.model';

import { CapaDocumentUpdateComponent } from './capa-document-update.component';

describe('CapaDocument Management Update Component', () => {
  let comp: CapaDocumentUpdateComponent;
  let fixture: ComponentFixture<CapaDocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let capaDocumentFormService: CapaDocumentFormService;
  let capaDocumentService: CapaDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CapaDocumentUpdateComponent],
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
      .overrideTemplate(CapaDocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CapaDocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    capaDocumentFormService = TestBed.inject(CapaDocumentFormService);
    capaDocumentService = TestBed.inject(CapaDocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const capaDocument: ICapaDocument = { id: 'CBA' };

      activatedRoute.data = of({ capaDocument });
      comp.ngOnInit();

      expect(comp.capaDocument).toEqual(capaDocument);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICapaDocument>>();
      const capaDocument = { id: 'ABC' };
      jest.spyOn(capaDocumentFormService, 'getCapaDocument').mockReturnValue(capaDocument);
      jest.spyOn(capaDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ capaDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: capaDocument }));
      saveSubject.complete();

      // THEN
      expect(capaDocumentFormService.getCapaDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(capaDocumentService.update).toHaveBeenCalledWith(expect.objectContaining(capaDocument));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICapaDocument>>();
      const capaDocument = { id: 'ABC' };
      jest.spyOn(capaDocumentFormService, 'getCapaDocument').mockReturnValue({ id: null });
      jest.spyOn(capaDocumentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ capaDocument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: capaDocument }));
      saveSubject.complete();

      // THEN
      expect(capaDocumentFormService.getCapaDocument).toHaveBeenCalled();
      expect(capaDocumentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICapaDocument>>();
      const capaDocument = { id: 'ABC' };
      jest.spyOn(capaDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ capaDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(capaDocumentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
