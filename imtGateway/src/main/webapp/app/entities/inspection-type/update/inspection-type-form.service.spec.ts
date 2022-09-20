import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../inspection-type.test-samples';

import { InspectionTypeFormService } from './inspection-type-form.service';

describe('InspectionType Form Service', () => {
  let service: InspectionTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InspectionTypeFormService);
  });

  describe('Service methods', () => {
    describe('createInspectionTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInspectionTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IInspectionType should create a new form with FormGroup', () => {
        const formGroup = service.createInspectionTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getInspectionType', () => {
      it('should return NewInspectionType for default InspectionType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInspectionTypeFormGroup(sampleWithNewData);

        const inspectionType = service.getInspectionType(formGroup) as any;

        expect(inspectionType).toMatchObject(sampleWithNewData);
      });

      it('should return NewInspectionType for empty InspectionType initial value', () => {
        const formGroup = service.createInspectionTypeFormGroup();

        const inspectionType = service.getInspectionType(formGroup) as any;

        expect(inspectionType).toMatchObject({});
      });

      it('should return IInspectionType', () => {
        const formGroup = service.createInspectionTypeFormGroup(sampleWithRequiredData);

        const inspectionType = service.getInspectionType(formGroup) as any;

        expect(inspectionType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInspectionType should not enable id FormControl', () => {
        const formGroup = service.createInspectionTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInspectionType should disable id FormControl', () => {
        const formGroup = service.createInspectionTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
