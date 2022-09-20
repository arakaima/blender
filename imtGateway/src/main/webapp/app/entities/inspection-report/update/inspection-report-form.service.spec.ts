import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../inspection-report.test-samples';

import { InspectionReportFormService } from './inspection-report-form.service';

describe('InspectionReport Form Service', () => {
  let service: InspectionReportFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InspectionReportFormService);
  });

  describe('Service methods', () => {
    describe('createInspectionReportFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInspectionReportFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IInspectionReport should create a new form with FormGroup', () => {
        const formGroup = service.createInspectionReportFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getInspectionReport', () => {
      it('should return NewInspectionReport for default InspectionReport initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInspectionReportFormGroup(sampleWithNewData);

        const inspectionReport = service.getInspectionReport(formGroup) as any;

        expect(inspectionReport).toMatchObject(sampleWithNewData);
      });

      it('should return NewInspectionReport for empty InspectionReport initial value', () => {
        const formGroup = service.createInspectionReportFormGroup();

        const inspectionReport = service.getInspectionReport(formGroup) as any;

        expect(inspectionReport).toMatchObject({});
      });

      it('should return IInspectionReport', () => {
        const formGroup = service.createInspectionReportFormGroup(sampleWithRequiredData);

        const inspectionReport = service.getInspectionReport(formGroup) as any;

        expect(inspectionReport).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInspectionReport should not enable id FormControl', () => {
        const formGroup = service.createInspectionReportFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInspectionReport should disable id FormControl', () => {
        const formGroup = service.createInspectionReportFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
