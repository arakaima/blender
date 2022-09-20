import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../requests-message.test-samples';

import { RequestsMessageFormService } from './requests-message-form.service';

describe('RequestsMessage Form Service', () => {
  let service: RequestsMessageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RequestsMessageFormService);
  });

  describe('Service methods', () => {
    describe('createRequestsMessageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRequestsMessageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IRequestsMessage should create a new form with FormGroup', () => {
        const formGroup = service.createRequestsMessageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getRequestsMessage', () => {
      it('should return NewRequestsMessage for default RequestsMessage initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRequestsMessageFormGroup(sampleWithNewData);

        const requestsMessage = service.getRequestsMessage(formGroup) as any;

        expect(requestsMessage).toMatchObject(sampleWithNewData);
      });

      it('should return NewRequestsMessage for empty RequestsMessage initial value', () => {
        const formGroup = service.createRequestsMessageFormGroup();

        const requestsMessage = service.getRequestsMessage(formGroup) as any;

        expect(requestsMessage).toMatchObject({});
      });

      it('should return IRequestsMessage', () => {
        const formGroup = service.createRequestsMessageFormGroup(sampleWithRequiredData);

        const requestsMessage = service.getRequestsMessage(formGroup) as any;

        expect(requestsMessage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRequestsMessage should not enable id FormControl', () => {
        const formGroup = service.createRequestsMessageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRequestsMessage should disable id FormControl', () => {
        const formGroup = service.createRequestsMessageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
