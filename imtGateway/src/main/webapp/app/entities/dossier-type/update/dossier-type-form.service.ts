import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDossierType, NewDossierType } from '../dossier-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDossierType for edit and NewDossierTypeFormGroupInput for create.
 */
type DossierTypeFormGroupInput = IDossierType | PartialWithRequiredKeyOf<NewDossierType>;

type DossierTypeFormDefaults = Pick<NewDossierType, 'id'>;

type DossierTypeFormGroupContent = {
  id: FormControl<IDossierType['id'] | NewDossierType['id']>;
};

export type DossierTypeFormGroup = FormGroup<DossierTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DossierTypeFormService {
  createDossierTypeFormGroup(dossierType: DossierTypeFormGroupInput = { id: null }): DossierTypeFormGroup {
    const dossierTypeRawValue = {
      ...this.getFormDefaults(),
      ...dossierType,
    };
    return new FormGroup<DossierTypeFormGroupContent>({
      id: new FormControl(
        { value: dossierTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getDossierType(form: DossierTypeFormGroup): IDossierType | NewDossierType {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IDossierType | NewDossierType;
  }

  resetForm(form: DossierTypeFormGroup, dossierType: DossierTypeFormGroupInput): void {
    const dossierTypeRawValue = { ...this.getFormDefaults(), ...dossierType };
    form.reset(
      {
        ...dossierTypeRawValue,
        id: { value: dossierTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DossierTypeFormDefaults {
    return {
      id: null,
    };
  }
}
