import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../deficiency.test-samples';

import { DeficiencyFormService } from './deficiency-form.service';

describe('Deficiency Form Service', () => {
  let service: DeficiencyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeficiencyFormService);
  });

  describe('Service methods', () => {
    describe('createDeficiencyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeficiencyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IDeficiency should create a new form with FormGroup', () => {
        const formGroup = service.createDeficiencyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getDeficiency', () => {
      it('should return NewDeficiency for default Deficiency initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDeficiencyFormGroup(sampleWithNewData);

        const deficiency = service.getDeficiency(formGroup) as any;

        expect(deficiency).toMatchObject(sampleWithNewData);
      });

      it('should return NewDeficiency for empty Deficiency initial value', () => {
        const formGroup = service.createDeficiencyFormGroup();

        const deficiency = service.getDeficiency(formGroup) as any;

        expect(deficiency).toMatchObject({});
      });

      it('should return IDeficiency', () => {
        const formGroup = service.createDeficiencyFormGroup(sampleWithRequiredData);

        const deficiency = service.getDeficiency(formGroup) as any;

        expect(deficiency).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDeficiency should not enable id FormControl', () => {
        const formGroup = service.createDeficiencyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDeficiency should disable id FormControl', () => {
        const formGroup = service.createDeficiencyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
