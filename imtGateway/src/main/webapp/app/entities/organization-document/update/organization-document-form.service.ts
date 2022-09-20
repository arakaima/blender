import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrganizationDocument, NewOrganizationDocument } from '../organization-document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrganizationDocument for edit and NewOrganizationDocumentFormGroupInput for create.
 */
type OrganizationDocumentFormGroupInput = IOrganizationDocument | PartialWithRequiredKeyOf<NewOrganizationDocument>;

type OrganizationDocumentFormDefaults = Pick<NewOrganizationDocument, 'id'>;

type OrganizationDocumentFormGroupContent = {
  id: FormControl<IOrganizationDocument['id'] | NewOrganizationDocument['id']>;
};

export type OrganizationDocumentFormGroup = FormGroup<OrganizationDocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrganizationDocumentFormService {
  createOrganizationDocumentFormGroup(
    organizationDocument: OrganizationDocumentFormGroupInput = { id: null }
  ): OrganizationDocumentFormGroup {
    const organizationDocumentRawValue = {
      ...this.getFormDefaults(),
      ...organizationDocument,
    };
    return new FormGroup<OrganizationDocumentFormGroupContent>({
      id: new FormControl(
        { value: organizationDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getOrganizationDocument(form: OrganizationDocumentFormGroup): IOrganizationDocument | NewOrganizationDocument {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IOrganizationDocument | NewOrganizationDocument;
  }

  resetForm(form: OrganizationDocumentFormGroup, organizationDocument: OrganizationDocumentFormGroupInput): void {
    const organizationDocumentRawValue = { ...this.getFormDefaults(), ...organizationDocument };
    form.reset(
      {
        ...organizationDocumentRawValue,
        id: { value: organizationDocumentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OrganizationDocumentFormDefaults {
    return {
      id: null,
    };
  }
}
