import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../capa.test-samples';

import { CapaFormService } from './capa-form.service';

describe('Capa Form Service', () => {
  let service: CapaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CapaFormService);
  });

  describe('Service methods', () => {
    describe('createCapaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCapaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing ICapa should create a new form with FormGroup', () => {
        const formGroup = service.createCapaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getCapa', () => {
      it('should return NewCapa for default Capa initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCapaFormGroup(sampleWithNewData);

        const capa = service.getCapa(formGroup) as any;

        expect(capa).toMatchObject(sampleWithNewData);
      });

      it('should return NewCapa for empty Capa initial value', () => {
        const formGroup = service.createCapaFormGroup();

        const capa = service.getCapa(formGroup) as any;

        expect(capa).toMatchObject({});
      });

      it('should return ICapa', () => {
        const formGroup = service.createCapaFormGroup(sampleWithRequiredData);

        const capa = service.getCapa(formGroup) as any;

        expect(capa).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICapa should not enable id FormControl', () => {
        const formGroup = service.createCapaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCapa should disable id FormControl', () => {
        const formGroup = service.createCapaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
