import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../attached-document.test-samples';

import { AttachedDocumentFormService } from './attached-document-form.service';

describe('AttachedDocument Form Service', () => {
  let service: AttachedDocumentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AttachedDocumentFormService);
  });

  describe('Service methods', () => {
    describe('createAttachedDocumentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAttachedDocumentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IAttachedDocument should create a new form with FormGroup', () => {
        const formGroup = service.createAttachedDocumentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getAttachedDocument', () => {
      it('should return NewAttachedDocument for default AttachedDocument initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAttachedDocumentFormGroup(sampleWithNewData);

        const attachedDocument = service.getAttachedDocument(formGroup) as any;

        expect(attachedDocument).toMatchObject(sampleWithNewData);
      });

      it('should return NewAttachedDocument for empty AttachedDocument initial value', () => {
        const formGroup = service.createAttachedDocumentFormGroup();

        const attachedDocument = service.getAttachedDocument(formGroup) as any;

        expect(attachedDocument).toMatchObject({});
      });

      it('should return IAttachedDocument', () => {
        const formGroup = service.createAttachedDocumentFormGroup(sampleWithRequiredData);

        const attachedDocument = service.getAttachedDocument(formGroup) as any;

        expect(attachedDocument).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAttachedDocument should not enable id FormControl', () => {
        const formGroup = service.createAttachedDocumentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAttachedDocument should disable id FormControl', () => {
        const formGroup = service.createAttachedDocumentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
