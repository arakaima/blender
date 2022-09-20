import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IContactPerson, NewContactPerson } from '../contact-person.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContactPerson for edit and NewContactPersonFormGroupInput for create.
 */
type ContactPersonFormGroupInput = IContactPerson | PartialWithRequiredKeyOf<NewContactPerson>;

type ContactPersonFormDefaults = Pick<NewContactPerson, 'id'>;

type ContactPersonFormGroupContent = {
  id: FormControl<IContactPerson['id'] | NewContactPerson['id']>;
};

export type ContactPersonFormGroup = FormGroup<ContactPersonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContactPersonFormService {
  createContactPersonFormGroup(contactPerson: ContactPersonFormGroupInput = { id: null }): ContactPersonFormGroup {
    const contactPersonRawValue = {
      ...this.getFormDefaults(),
      ...contactPerson,
    };
    return new FormGroup<ContactPersonFormGroupContent>({
      id: new FormControl(
        { value: contactPersonRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getContactPerson(form: ContactPersonFormGroup): IContactPerson | NewContactPerson {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IContactPerson | NewContactPerson;
  }

  resetForm(form: ContactPersonFormGroup, contactPerson: ContactPersonFormGroupInput): void {
    const contactPersonRawValue = { ...this.getFormDefaults(), ...contactPerson };
    form.reset(
      {
        ...contactPersonRawValue,
        id: { value: contactPersonRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ContactPersonFormDefaults {
    return {
      id: null,
    };
  }
}
