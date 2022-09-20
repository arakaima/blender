import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInspectorDossier, NewInspectorDossier } from '../inspector-dossier.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInspectorDossier for edit and NewInspectorDossierFormGroupInput for create.
 */
type InspectorDossierFormGroupInput = IInspectorDossier | PartialWithRequiredKeyOf<NewInspectorDossier>;

type InspectorDossierFormDefaults = Pick<NewInspectorDossier, 'id'>;

type InspectorDossierFormGroupContent = {
  id: FormControl<IInspectorDossier['id'] | NewInspectorDossier['id']>;
};

export type InspectorDossierFormGroup = FormGroup<InspectorDossierFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InspectorDossierFormService {
  createInspectorDossierFormGroup(inspectorDossier: InspectorDossierFormGroupInput = { id: null }): InspectorDossierFormGroup {
    const inspectorDossierRawValue = {
      ...this.getFormDefaults(),
      ...inspectorDossier,
    };
    return new FormGroup<InspectorDossierFormGroupContent>({
      id: new FormControl(
        { value: inspectorDossierRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getInspectorDossier(form: InspectorDossierFormGroup): IInspectorDossier | NewInspectorDossier {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IInspectorDossier | NewInspectorDossier;
  }

  resetForm(form: InspectorDossierFormGroup, inspectorDossier: InspectorDossierFormGroupInput): void {
    const inspectorDossierRawValue = { ...this.getFormDefaults(), ...inspectorDossier };
    form.reset(
      {
        ...inspectorDossierRawValue,
        id: { value: inspectorDossierRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InspectorDossierFormDefaults {
    return {
      id: null,
    };
  }
}
