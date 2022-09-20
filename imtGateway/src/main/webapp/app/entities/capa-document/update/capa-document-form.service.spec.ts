import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../capa-document.test-samples';

import { CapaDocumentFormService } from './capa-document-form.service';

describe('CapaDocument Form Service', () => {
  let service: CapaDocumentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CapaDocumentFormService);
  });

  describe('Service methods', () => {
    describe('createCapaDocumentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCapaDocumentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing ICapaDocument should create a new form with FormGroup', () => {
        const formGroup = service.createCapaDocumentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getCapaDocument', () => {
      it('should return NewCapaDocument for default CapaDocument initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCapaDocumentFormGroup(sampleWithNewData);

        const capaDocument = service.getCapaDocument(formGroup) as any;

        expect(capaDocument).toMatchObject(sampleWithNewData);
      });

      it('should return NewCapaDocument for empty CapaDocument initial value', () => {
        const formGroup = service.createCapaDocumentFormGroup();

        const capaDocument = service.getCapaDocument(formGroup) as any;

        expect(capaDocument).toMatchObject({});
      });

      it('should return ICapaDocument', () => {
        const formGroup = service.createCapaDocumentFormGroup(sampleWithRequiredData);

        const capaDocument = service.getCapaDocument(formGroup) as any;

        expect(capaDocument).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICapaDocument should not enable id FormControl', () => {
        const formGroup = service.createCapaDocumentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCapaDocument should disable id FormControl', () => {
        const formGroup = service.createCapaDocumentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
