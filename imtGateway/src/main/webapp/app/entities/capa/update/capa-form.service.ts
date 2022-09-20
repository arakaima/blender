import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICapa, NewCapa } from '../capa.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICapa for edit and NewCapaFormGroupInput for create.
 */
type CapaFormGroupInput = ICapa | PartialWithRequiredKeyOf<NewCapa>;

type CapaFormDefaults = Pick<NewCapa, 'id'>;

type CapaFormGroupContent = {
  id: FormControl<ICapa['id'] | NewCapa['id']>;
};

export type CapaFormGroup = FormGroup<CapaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CapaFormService {
  createCapaFormGroup(capa: CapaFormGroupInput = { id: null }): CapaFormGroup {
    const capaRawValue = {
      ...this.getFormDefaults(),
      ...capa,
    };
    return new FormGroup<CapaFormGroupContent>({
      id: new FormControl(
        { value: capaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getCapa(form: CapaFormGroup): ICapa | NewCapa {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as ICapa | NewCapa;
  }

  resetForm(form: CapaFormGroup, capa: CapaFormGroupInput): void {
    const capaRawValue = { ...this.getFormDefaults(), ...capa };
    form.reset(
      {
        ...capaRawValue,
        id: { value: capaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CapaFormDefaults {
    return {
      id: null,
    };
  }
}
