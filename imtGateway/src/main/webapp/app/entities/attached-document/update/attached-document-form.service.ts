import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAttachedDocument, NewAttachedDocument } from '../attached-document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAttachedDocument for edit and NewAttachedDocumentFormGroupInput for create.
 */
type AttachedDocumentFormGroupInput = IAttachedDocument | PartialWithRequiredKeyOf<NewAttachedDocument>;

type AttachedDocumentFormDefaults = Pick<NewAttachedDocument, 'id'>;

type AttachedDocumentFormGroupContent = {
  id: FormControl<IAttachedDocument['id'] | NewAttachedDocument['id']>;
};

export type AttachedDocumentFormGroup = FormGroup<AttachedDocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AttachedDocumentFormService {
  createAttachedDocumentFormGroup(attachedDocument: AttachedDocumentFormGroupInput = { id: null }): AttachedDocumentFormGroup {
    const attachedDocumentRawValue = {
      ...this.getFormDefaults(),
      ...attachedDocument,
    };
    return new FormGroup<AttachedDocumentFormGroupContent>({
      id: new FormControl(
        { value: attachedDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getAttachedDocument(form: AttachedDocumentFormGroup): IAttachedDocument | NewAttachedDocument {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IAttachedDocument | NewAttachedDocument;
  }

  resetForm(form: AttachedDocumentFormGroup, attachedDocument: AttachedDocumentFormGroupInput): void {
    const attachedDocumentRawValue = { ...this.getFormDefaults(), ...attachedDocument };
    form.reset(
      {
        ...attachedDocumentRawValue,
        id: { value: attachedDocumentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AttachedDocumentFormDefaults {
    return {
      id: null,
    };
  }
}
