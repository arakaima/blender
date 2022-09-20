import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InspectionFormService } from './inspection-form.service';
import { InspectionService } from '../service/inspection.service';
import { IInspection } from '../inspection.model';

import { InspectionUpdateComponent } from './inspection-update.component';

describe('Inspection Management Update Component', () => {
  let comp: InspectionUpdateComponent;
  let fixture: ComponentFixture<InspectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inspectionFormService: InspectionFormService;
  let inspectionService: InspectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InspectionUpdateComponent],
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
      .overrideTemplate(InspectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InspectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inspectionFormService = TestBed.inject(InspectionFormService);
    inspectionService = TestBed.inject(InspectionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const inspection: IInspection = { id: 'CBA' };

      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      expect(comp.inspection).toEqual(inspection);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspection>>();
      const inspection = { id: 'ABC' };
      jest.spyOn(inspectionFormService, 'getInspection').mockReturnValue(inspection);
      jest.spyOn(inspectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspection }));
      saveSubject.complete();

      // THEN
      expect(inspectionFormService.getInspection).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inspectionService.update).toHaveBeenCalledWith(expect.objectContaining(inspection));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspection>>();
      const inspection = { id: 'ABC' };
      jest.spyOn(inspectionFormService, 'getInspection').mockReturnValue({ id: null });
      jest.spyOn(inspectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspection: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspection }));
      saveSubject.complete();

      // THEN
      expect(inspectionFormService.getInspection).toHaveBeenCalled();
      expect(inspectionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspection>>();
      const inspection = { id: 'ABC' };
      jest.spyOn(inspectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inspectionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
