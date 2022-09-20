import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRiskAssessment, NewRiskAssessment } from '../risk-assessment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRiskAssessment for edit and NewRiskAssessmentFormGroupInput for create.
 */
type RiskAssessmentFormGroupInput = IRiskAssessment | PartialWithRequiredKeyOf<NewRiskAssessment>;

type RiskAssessmentFormDefaults = Pick<NewRiskAssessment, 'id'>;

type RiskAssessmentFormGroupContent = {
  id: FormControl<IRiskAssessment['id'] | NewRiskAssessment['id']>;
};

export type RiskAssessmentFormGroup = FormGroup<RiskAssessmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RiskAssessmentFormService {
  createRiskAssessmentFormGroup(riskAssessment: RiskAssessmentFormGroupInput = { id: null }): RiskAssessmentFormGroup {
    const riskAssessmentRawValue = {
      ...this.getFormDefaults(),
      ...riskAssessment,
    };
    return new FormGroup<RiskAssessmentFormGroupContent>({
      id: new FormControl(
        { value: riskAssessmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getRiskAssessment(form: RiskAssessmentFormGroup): IRiskAssessment | NewRiskAssessment {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IRiskAssessment | NewRiskAssessment;
  }

  resetForm(form: RiskAssessmentFormGroup, riskAssessment: RiskAssessmentFormGroupInput): void {
    const riskAssessmentRawValue = { ...this.getFormDefaults(), ...riskAssessment };
    form.reset(
      {
        ...riskAssessmentRawValue,
        id: { value: riskAssessmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RiskAssessmentFormDefaults {
    return {
      id: null,
    };
  }
}
