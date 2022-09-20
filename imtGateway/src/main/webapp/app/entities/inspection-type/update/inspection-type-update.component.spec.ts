import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InspectionTypeFormService } from './inspection-type-form.service';
import { InspectionTypeService } from '../service/inspection-type.service';
import { IInspectionType } from '../inspection-type.model';

import { InspectionTypeUpdateComponent } from './inspection-type-update.component';

describe('InspectionType Management Update Component', () => {
  let comp: InspectionTypeUpdateComponent;
  let fixture: ComponentFixture<InspectionTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inspectionTypeFormService: InspectionTypeFormService;
  let inspectionTypeService: InspectionTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InspectionTypeUpdateComponent],
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
      .overrideTemplate(InspectionTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InspectionTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inspectionTypeFormService = TestBed.inject(InspectionTypeFormService);
    inspectionTypeService = TestBed.inject(InspectionTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const inspectionType: IInspectionType = { id: 'CBA' };

      activatedRoute.data = of({ inspectionType });
      comp.ngOnInit();

      expect(comp.inspectionType).toEqual(inspectionType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspectionType>>();
      const inspectionType = { id: 'ABC' };
      jest.spyOn(inspectionTypeFormService, 'getInspectionType').mockReturnValue(inspectionType);
      jest.spyOn(inspectionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspectionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspectionType }));
      saveSubject.complete();

      // THEN
      expect(inspectionTypeFormService.getInspectionType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inspectionTypeService.update).toHaveBeenCalledWith(expect.objectContaining(inspectionType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspectionType>>();
      const inspectionType = { id: 'ABC' };
      jest.spyOn(inspectionTypeFormService, 'getInspectionType').mockReturnValue({ id: null });
      jest.spyOn(inspectionTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspectionType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspectionType }));
      saveSubject.complete();

      // THEN
      expect(inspectionTypeFormService.getInspectionType).toHaveBeenCalled();
      expect(inspectionTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspectionType>>();
      const inspectionType = { id: 'ABC' };
      jest.spyOn(inspectionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspectionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inspectionTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
