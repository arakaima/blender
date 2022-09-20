import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../risk-assessment.test-samples';

import { RiskAssessmentFormService } from './risk-assessment-form.service';

describe('RiskAssessment Form Service', () => {
  let service: RiskAssessmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RiskAssessmentFormService);
  });

  describe('Service methods', () => {
    describe('createRiskAssessmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRiskAssessmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IRiskAssessment should create a new form with FormGroup', () => {
        const formGroup = service.createRiskAssessmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getRiskAssessment', () => {
      it('should return NewRiskAssessment for default RiskAssessment initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRiskAssessmentFormGroup(sampleWithNewData);

        const riskAssessment = service.getRiskAssessment(formGroup) as any;

        expect(riskAssessment).toMatchObject(sampleWithNewData);
      });

      it('should return NewRiskAssessment for empty RiskAssessment initial value', () => {
        const formGroup = service.createRiskAssessmentFormGroup();

        const riskAssessment = service.getRiskAssessment(formGroup) as any;

        expect(riskAssessment).toMatchObject({});
      });

      it('should return IRiskAssessment', () => {
        const formGroup = service.createRiskAssessmentFormGroup(sampleWithRequiredData);

        const riskAssessment = service.getRiskAssessment(formGroup) as any;

        expect(riskAssessment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRiskAssessment should not enable id FormControl', () => {
        const formGroup = service.createRiskAssessmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRiskAssessment should disable id FormControl', () => {
        const formGroup = service.createRiskAssessmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
