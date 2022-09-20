import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICapaDocument, NewCapaDocument } from '../capa-document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICapaDocument for edit and NewCapaDocumentFormGroupInput for create.
 */
type CapaDocumentFormGroupInput = ICapaDocument | PartialWithRequiredKeyOf<NewCapaDocument>;

type CapaDocumentFormDefaults = Pick<NewCapaDocument, 'id'>;

type CapaDocumentFormGroupContent = {
  id: FormControl<ICapaDocument['id'] | NewCapaDocument['id']>;
};

export type CapaDocumentFormGroup = FormGroup<CapaDocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CapaDocumentFormService {
  createCapaDocumentFormGroup(capaDocument: CapaDocumentFormGroupInput = { id: null }): CapaDocumentFormGroup {
    const capaDocumentRawValue = {
      ...this.getFormDefaults(),
      ...capaDocument,
    };
    return new FormGroup<CapaDocumentFormGroupContent>({
      id: new FormControl(
        { value: capaDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getCapaDocument(form: CapaDocumentFormGroup): ICapaDocument | NewCapaDocument {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as ICapaDocument | NewCapaDocument;
  }

  resetForm(form: CapaDocumentFormGroup, capaDocument: CapaDocumentFormGroupInput): void {
    const capaDocumentRawValue = { ...this.getFormDefaults(), ...capaDocument };
    form.reset(
      {
        ...capaDocumentRawValue,
        id: { value: capaDocumentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CapaDocumentFormDefaults {
    return {
      id: null,
    };
  }
}
