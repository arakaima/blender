import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDeficiency, NewDeficiency } from '../deficiency.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDeficiency for edit and NewDeficiencyFormGroupInput for create.
 */
type DeficiencyFormGroupInput = IDeficiency | PartialWithRequiredKeyOf<NewDeficiency>;

type DeficiencyFormDefaults = Pick<NewDeficiency, 'id'>;

type DeficiencyFormGroupContent = {
  id: FormControl<IDeficiency['id'] | NewDeficiency['id']>;
};

export type DeficiencyFormGroup = FormGroup<DeficiencyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeficiencyFormService {
  createDeficiencyFormGroup(deficiency: DeficiencyFormGroupInput = { id: null }): DeficiencyFormGroup {
    const deficiencyRawValue = {
      ...this.getFormDefaults(),
      ...deficiency,
    };
    return new FormGroup<DeficiencyFormGroupContent>({
      id: new FormControl(
        { value: deficiencyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getDeficiency(form: DeficiencyFormGroup): IDeficiency | NewDeficiency {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IDeficiency | NewDeficiency;
  }

  resetForm(form: DeficiencyFormGroup, deficiency: DeficiencyFormGroupInput): void {
    const deficiencyRawValue = { ...this.getFormDefaults(), ...deficiency };
    form.reset(
      {
        ...deficiencyRawValue,
        id: { value: deficiencyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DeficiencyFormDefaults {
    return {
      id: null,
    };
  }
}
