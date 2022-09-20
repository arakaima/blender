import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../dossier-type.test-samples';

import { DossierTypeFormService } from './dossier-type-form.service';

describe('DossierType Form Service', () => {
  let service: DossierTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DossierTypeFormService);
  });

  describe('Service methods', () => {
    describe('createDossierTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDossierTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IDossierType should create a new form with FormGroup', () => {
        const formGroup = service.createDossierTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getDossierType', () => {
      it('should return NewDossierType for default DossierType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDossierTypeFormGroup(sampleWithNewData);

        const dossierType = service.getDossierType(formGroup) as any;

        expect(dossierType).toMatchObject(sampleWithNewData);
      });

      it('should return NewDossierType for empty DossierType initial value', () => {
        const formGroup = service.createDossierTypeFormGroup();

        const dossierType = service.getDossierType(formGroup) as any;

        expect(dossierType).toMatchObject({});
      });

      it('should return IDossierType', () => {
        const formGroup = service.createDossierTypeFormGroup(sampleWithRequiredData);

        const dossierType = service.getDossierType(formGroup) as any;

        expect(dossierType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDossierType should not enable id FormControl', () => {
        const formGroup = service.createDossierTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDossierType should disable id FormControl', () => {
        const formGroup = service.createDossierTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
