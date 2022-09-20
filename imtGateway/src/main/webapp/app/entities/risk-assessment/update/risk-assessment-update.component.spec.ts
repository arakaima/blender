import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RiskAssessmentFormService } from './risk-assessment-form.service';
import { RiskAssessmentService } from '../service/risk-assessment.service';
import { IRiskAssessment } from '../risk-assessment.model';

import { RiskAssessmentUpdateComponent } from './risk-assessment-update.component';

describe('RiskAssessment Management Update Component', () => {
  let comp: RiskAssessmentUpdateComponent;
  let fixture: ComponentFixture<RiskAssessmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let riskAssessmentFormService: RiskAssessmentFormService;
  let riskAssessmentService: RiskAssessmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RiskAssessmentUpdateComponent],
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
      .overrideTemplate(RiskAssessmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RiskAssessmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    riskAssessmentFormService = TestBed.inject(RiskAssessmentFormService);
    riskAssessmentService = TestBed.inject(RiskAssessmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const riskAssessment: IRiskAssessment = { id: 'CBA' };

      activatedRoute.data = of({ riskAssessment });
      comp.ngOnInit();

      expect(comp.riskAssessment).toEqual(riskAssessment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRiskAssessment>>();
      const riskAssessment = { id: 'ABC' };
      jest.spyOn(riskAssessmentFormService, 'getRiskAssessment').mockReturnValue(riskAssessment);
      jest.spyOn(riskAssessmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ riskAssessment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: riskAssessment }));
      saveSubject.complete();

      // THEN
      expect(riskAssessmentFormService.getRiskAssessment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(riskAssessmentService.update).toHaveBeenCalledWith(expect.objectContaining(riskAssessment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRiskAssessment>>();
      const riskAssessment = { id: 'ABC' };
      jest.spyOn(riskAssessmentFormService, 'getRiskAssessment').mockReturnValue({ id: null });
      jest.spyOn(riskAssessmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ riskAssessment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: riskAssessment }));
      saveSubject.complete();

      // THEN
      expect(riskAssessmentFormService.getRiskAssessment).toHaveBeenCalled();
      expect(riskAssessmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRiskAssessment>>();
      const riskAssessment = { id: 'ABC' };
      jest.spyOn(riskAssessmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ riskAssessment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(riskAssessmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
