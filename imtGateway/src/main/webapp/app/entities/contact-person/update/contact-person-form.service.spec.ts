import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../contact-person.test-samples';

import { ContactPersonFormService } from './contact-person-form.service';

describe('ContactPerson Form Service', () => {
  let service: ContactPersonFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContactPersonFormService);
  });

  describe('Service methods', () => {
    describe('createContactPersonFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContactPersonFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing IContactPerson should create a new form with FormGroup', () => {
        const formGroup = service.createContactPersonFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getContactPerson', () => {
      it('should return NewContactPerson for default ContactPerson initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createContactPersonFormGroup(sampleWithNewData);

        const contactPerson = service.getContactPerson(formGroup) as any;

        expect(contactPerson).toMatchObject(sampleWithNewData);
      });

      it('should return NewContactPerson for empty ContactPerson initial value', () => {
        const formGroup = service.createContactPersonFormGroup();

        const contactPerson = service.getContactPerson(formGroup) as any;

        expect(contactPerson).toMatchObject({});
      });

      it('should return IContactPerson', () => {
        const formGroup = service.createContactPersonFormGroup(sampleWithRequiredData);

        const contactPerson = service.getContactPerson(formGroup) as any;

        expect(contactPerson).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContactPerson should not enable id FormControl', () => {
        const formGroup = service.createContactPersonFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContactPerson should disable id FormControl', () => {
        const formGroup = service.createContactPersonFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
