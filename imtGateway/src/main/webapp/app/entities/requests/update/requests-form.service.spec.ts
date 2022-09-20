import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../requests.test-samples';

import { RequestsFormService } from './requests-form.service';

describe('Requests Form Service', () => {
  let service: RequestsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RequestsFormService);
  });

  describe('Service methods', () => {
    describe('createRequestsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRequestsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IRequests should create a new form with FormGroup', () => {
        const formGroup = service.createRequestsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getRequests', () => {
      it('should return NewRequests for default Requests initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRequestsFormGroup(sampleWithNewData);

        const requests = service.getRequests(formGroup) as any;

        expect(requests).toMatchObject(sampleWithNewData);
      });

      it('should return NewRequests for empty Requests initial value', () => {
        const formGroup = service.createRequestsFormGroup();

        const requests = service.getRequests(formGroup) as any;

        expect(requests).toMatchObject({});
      });

      it('should return IRequests', () => {
        const formGroup = service.createRequestsFormGroup(sampleWithRequiredData);

        const requests = service.getRequests(formGroup) as any;

        expect(requests).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRequests should not enable id FormControl', () => {
        const formGroup = service.createRequestsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRequests should disable id FormControl', () => {
        const formGroup = service.createRequestsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
