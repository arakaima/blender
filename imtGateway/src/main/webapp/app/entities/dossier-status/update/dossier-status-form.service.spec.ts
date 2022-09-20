import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../dossier-status.test-samples';

import { DossierStatusFormService } from './dossier-status-form.service';

describe('DossierStatus Form Service', () => {
  let service: DossierStatusFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DossierStatusFormService);
  });

  describe('Service methods', () => {
    describe('createDossierStatusFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDossierStatusFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IDossierStatus should create a new form with FormGroup', () => {
        const formGroup = service.createDossierStatusFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getDossierStatus', () => {
      it('should return NewDossierStatus for default DossierStatus initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDossierStatusFormGroup(sampleWithNewData);

        const dossierStatus = service.getDossierStatus(formGroup) as any;

        expect(dossierStatus).toMatchObject(sampleWithNewData);
      });

      it('should return NewDossierStatus for empty DossierStatus initial value', () => {
        const formGroup = service.createDossierStatusFormGroup();

        const dossierStatus = service.getDossierStatus(formGroup) as any;

        expect(dossierStatus).toMatchObject({});
      });

      it('should return IDossierStatus', () => {
        const formGroup = service.createDossierStatusFormGroup(sampleWithRequiredData);

        const dossierStatus = service.getDossierStatus(formGroup) as any;

        expect(dossierStatus).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDossierStatus should not enable id FormControl', () => {
        const formGroup = service.createDossierStatusFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDossierStatus should disable id FormControl', () => {
        const formGroup = service.createDossierStatusFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
