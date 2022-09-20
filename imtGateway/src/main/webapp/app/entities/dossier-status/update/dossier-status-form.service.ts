import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDossierStatus, NewDossierStatus } from '../dossier-status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDossierStatus for edit and NewDossierStatusFormGroupInput for create.
 */
type DossierStatusFormGroupInput = IDossierStatus | PartialWithRequiredKeyOf<NewDossierStatus>;

type DossierStatusFormDefaults = Pick<NewDossierStatus, 'id'>;

type DossierStatusFormGroupContent = {
  id: FormControl<IDossierStatus['id'] | NewDossierStatus['id']>;
};

export type DossierStatusFormGroup = FormGroup<DossierStatusFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DossierStatusFormService {
  createDossierStatusFormGroup(dossierStatus: DossierStatusFormGroupInput = { id: null }): DossierStatusFormGroup {
    const dossierStatusRawValue = {
      ...this.getFormDefaults(),
      ...dossierStatus,
    };
    return new FormGroup<DossierStatusFormGroupContent>({
      id: new FormControl(
        { value: dossierStatusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getDossierStatus(form: DossierStatusFormGroup): IDossierStatus | NewDossierStatus {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IDossierStatus | NewDossierStatus;
  }

  resetForm(form: DossierStatusFormGroup, dossierStatus: DossierStatusFormGroupInput): void {
    const dossierStatusRawValue = { ...this.getFormDefaults(), ...dossierStatus };
    form.reset(
      {
        ...dossierStatusRawValue,
        id: { value: dossierStatusRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DossierStatusFormDefaults {
    return {
      id: null,
    };
  }
}
