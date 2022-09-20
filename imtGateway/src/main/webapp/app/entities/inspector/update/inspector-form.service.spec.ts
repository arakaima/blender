import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../inspector.test-samples';

import { InspectorFormService } from './inspector-form.service';

describe('Inspector Form Service', () => {
  let service: InspectorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InspectorFormService);
  });

  describe('Service methods', () => {
    describe('createInspectorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInspectorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IInspector should create a new form with FormGroup', () => {
        const formGroup = service.createInspectorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getInspector', () => {
      it('should return NewInspector for default Inspector initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInspectorFormGroup(sampleWithNewData);

        const inspector = service.getInspector(formGroup) as any;

        expect(inspector).toMatchObject(sampleWithNewData);
      });

      it('should return NewInspector for empty Inspector initial value', () => {
        const formGroup = service.createInspectorFormGroup();

        const inspector = service.getInspector(formGroup) as any;

        expect(inspector).toMatchObject({});
      });

      it('should return IInspector', () => {
        const formGroup = service.createInspectorFormGroup(sampleWithRequiredData);

        const inspector = service.getInspector(formGroup) as any;

        expect(inspector).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInspector should not enable id FormControl', () => {
        const formGroup = service.createInspectorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInspector should disable id FormControl', () => {
        const formGroup = service.createInspectorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
