import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../organization-document.test-samples';

import { OrganizationDocumentFormService } from './organization-document-form.service';

describe('OrganizationDocument Form Service', () => {
  let service: OrganizationDocumentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrganizationDocumentFormService);
  });

  describe('Service methods', () => {
    describe('createOrganizationDocumentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrganizationDocumentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IOrganizationDocument should create a new form with FormGroup', () => {
        const formGroup = service.createOrganizationDocumentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getOrganizationDocument', () => {
      it('should return NewOrganizationDocument for default OrganizationDocument initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOrganizationDocumentFormGroup(sampleWithNewData);

        const organizationDocument = service.getOrganizationDocument(formGroup) as any;

        expect(organizationDocument).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrganizationDocument for empty OrganizationDocument initial value', () => {
        const formGroup = service.createOrganizationDocumentFormGroup();

        const organizationDocument = service.getOrganizationDocument(formGroup) as any;

        expect(organizationDocument).toMatchObject({});
      });

      it('should return IOrganizationDocument', () => {
        const formGroup = service.createOrganizationDocumentFormGroup(sampleWithRequiredData);

        const organizationDocument = service.getOrganizationDocument(formGroup) as any;

        expect(organizationDocument).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrganizationDocument should not enable id FormControl', () => {
        const formGroup = service.createOrganizationDocumentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrganizationDocument should disable id FormControl', () => {
        const formGroup = service.createOrganizationDocumentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
