import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../inspector-dossier.test-samples';

import { InspectorDossierFormService } from './inspector-dossier-form.service';

describe('InspectorDossier Form Service', () => {
  let service: InspectorDossierFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InspectorDossierFormService);
  });

  describe('Service methods', () => {
    describe('createInspectorDossierFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInspectorDossierFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IInspectorDossier should create a new form with FormGroup', () => {
        const formGroup = service.createInspectorDossierFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getInspectorDossier', () => {
      it('should return NewInspectorDossier for default InspectorDossier initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInspectorDossierFormGroup(sampleWithNewData);

        const inspectorDossier = service.getInspectorDossier(formGroup) as any;

        expect(inspectorDossier).toMatchObject(sampleWithNewData);
      });

      it('should return NewInspectorDossier for empty InspectorDossier initial value', () => {
        const formGroup = service.createInspectorDossierFormGroup();

        const inspectorDossier = service.getInspectorDossier(formGroup) as any;

        expect(inspectorDossier).toMatchObject({});
      });

      it('should return IInspectorDossier', () => {
        const formGroup = service.createInspectorDossierFormGroup(sampleWithRequiredData);

        const inspectorDossier = service.getInspectorDossier(formGroup) as any;

        expect(inspectorDossier).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInspectorDossier should not enable id FormControl', () => {
        const formGroup = service.createInspectorDossierFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInspectorDossier should disable id FormControl', () => {
        const formGroup = service.createInspectorDossierFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
