import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InspectionReportFormService } from './inspection-report-form.service';
import { InspectionReportService } from '../service/inspection-report.service';
import { IInspectionReport } from '../inspection-report.model';

import { InspectionReportUpdateComponent } from './inspection-report-update.component';

describe('InspectionReport Management Update Component', () => {
  let comp: InspectionReportUpdateComponent;
  let fixture: ComponentFixture<InspectionReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inspectionReportFormService: InspectionReportFormService;
  let inspectionReportService: InspectionReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InspectionReportUpdateComponent],
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
      .overrideTemplate(InspectionReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InspectionReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inspectionReportFormService = TestBed.inject(InspectionReportFormService);
    inspectionReportService = TestBed.inject(InspectionReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const inspectionReport: IInspectionReport = { id: 'CBA' };

      activatedRoute.data = of({ inspectionReport });
      comp.ngOnInit();

      expect(comp.inspectionReport).toEqual(inspectionReport);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspectionReport>>();
      const inspectionReport = { id: 'ABC' };
      jest.spyOn(inspectionReportFormService, 'getInspectionReport').mockReturnValue(inspectionReport);
      jest.spyOn(inspectionReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspectionReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspectionReport }));
      saveSubject.complete();

      // THEN
      expect(inspectionReportFormService.getInspectionReport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inspectionReportService.update).toHaveBeenCalledWith(expect.objectContaining(inspectionReport));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspectionReport>>();
      const inspectionReport = { id: 'ABC' };
      jest.spyOn(inspectionReportFormService, 'getInspectionReport').mockReturnValue({ id: null });
      jest.spyOn(inspectionReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspectionReport: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspectionReport }));
      saveSubject.complete();

      // THEN
      expect(inspectionReportFormService.getInspectionReport).toHaveBeenCalled();
      expect(inspectionReportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspectionReport>>();
      const inspectionReport = { id: 'ABC' };
      jest.spyOn(inspectionReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspectionReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inspectionReportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
