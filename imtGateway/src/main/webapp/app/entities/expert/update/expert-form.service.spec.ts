import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../expert.test-samples';

import { ExpertFormService } from './expert-form.service';

describe('Expert Form Service', () => {
  let service: ExpertFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpertFormService);
  });

  describe('Service methods', () => {
    describe('createExpertFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExpertFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IExpert should create a new form with FormGroup', () => {
        const formGroup = service.createExpertFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getExpert', () => {
      it('should return NewExpert for default Expert initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createExpertFormGroup(sampleWithNewData);

        const expert = service.getExpert(formGroup) as any;

        expect(expert).toMatchObject(sampleWithNewData);
      });

      it('should return NewExpert for empty Expert initial value', () => {
        const formGroup = service.createExpertFormGroup();

        const expert = service.getExpert(formGroup) as any;

        expect(expert).toMatchObject({});
      });

      it('should return IExpert', () => {
        const formGroup = service.createExpertFormGroup(sampleWithRequiredData);

        const expert = service.getExpert(formGroup) as any;

        expect(expert).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExpert should not enable id FormControl', () => {
        const formGroup = service.createExpertFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExpert should disable id FormControl', () => {
        const formGroup = service.createExpertFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
