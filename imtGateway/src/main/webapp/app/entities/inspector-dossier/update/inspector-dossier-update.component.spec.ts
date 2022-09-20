import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InspectorDossierFormService } from './inspector-dossier-form.service';
import { InspectorDossierService } from '../service/inspector-dossier.service';
import { IInspectorDossier } from '../inspector-dossier.model';

import { InspectorDossierUpdateComponent } from './inspector-dossier-update.component';

describe('InspectorDossier Management Update Component', () => {
  let comp: InspectorDossierUpdateComponent;
  let fixture: ComponentFixture<InspectorDossierUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inspectorDossierFormService: InspectorDossierFormService;
  let inspectorDossierService: InspectorDossierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InspectorDossierUpdateComponent],
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
      .overrideTemplate(InspectorDossierUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InspectorDossierUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inspectorDossierFormService = TestBed.inject(InspectorDossierFormService);
    inspectorDossierService = TestBed.inject(InspectorDossierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const inspectorDossier: IInspectorDossier = { id: 'CBA' };

      activatedRoute.data = of({ inspectorDossier });
      comp.ngOnInit();

      expect(comp.inspectorDossier).toEqual(inspectorDossier);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspectorDossier>>();
      const inspectorDossier = { id: 'ABC' };
      jest.spyOn(inspectorDossierFormService, 'getInspectorDossier').mockReturnValue(inspectorDossier);
      jest.spyOn(inspectorDossierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspectorDossier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspectorDossier }));
      saveSubject.complete();

      // THEN
      expect(inspectorDossierFormService.getInspectorDossier).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inspectorDossierService.update).toHaveBeenCalledWith(expect.objectContaining(inspectorDossier));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspectorDossier>>();
      const inspectorDossier = { id: 'ABC' };
      jest.spyOn(inspectorDossierFormService, 'getInspectorDossier').mockReturnValue({ id: null });
      jest.spyOn(inspectorDossierService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspectorDossier: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspectorDossier }));
      saveSubject.complete();

      // THEN
      expect(inspectorDossierFormService.getInspectorDossier).toHaveBeenCalled();
      expect(inspectorDossierService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspectorDossier>>();
      const inspectorDossier = { id: 'ABC' };
      jest.spyOn(inspectorDossierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspectorDossier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inspectorDossierService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
