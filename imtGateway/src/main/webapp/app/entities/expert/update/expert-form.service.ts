import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExpert, NewExpert } from '../expert.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpert for edit and NewExpertFormGroupInput for create.
 */
type ExpertFormGroupInput = IExpert | PartialWithRequiredKeyOf<NewExpert>;

type ExpertFormDefaults = Pick<NewExpert, 'id'>;

type ExpertFormGroupContent = {
  id: FormControl<IExpert['id'] | NewExpert['id']>;
};

export type ExpertFormGroup = FormGroup<ExpertFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpertFormService {
  createExpertFormGroup(expert: ExpertFormGroupInput = { id: null }): ExpertFormGroup {
    const expertRawValue = {
      ...this.getFormDefaults(),
      ...expert,
    };
    return new FormGroup<ExpertFormGroupContent>({
      id: new FormControl(
        { value: expertRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getExpert(form: ExpertFormGroup): IExpert | NewExpert {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IExpert | NewExpert;
  }

  resetForm(form: ExpertFormGroup, expert: ExpertFormGroupInput): void {
    const expertRawValue = { ...this.getFormDefaults(), ...expert };
    form.reset(
      {
        ...expertRawValue,
        id: { value: expertRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ExpertFormDefaults {
    return {
      id: null,
    };
  }
}
