import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInspection, NewInspection } from '../inspection.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInspection for edit and NewInspectionFormGroupInput for create.
 */
type InspectionFormGroupInput = IInspection | PartialWithRequiredKeyOf<NewInspection>;

type InspectionFormDefaults = Pick<NewInspection, 'id'>;

type InspectionFormGroupContent = {
  id: FormControl<IInspection['id'] | NewInspection['id']>;
};

export type InspectionFormGroup = FormGroup<InspectionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InspectionFormService {
  createInspectionFormGroup(inspection: InspectionFormGroupInput = { id: null }): InspectionFormGroup {
    const inspectionRawValue = {
      ...this.getFormDefaults(),
      ...inspection,
    };
    return new FormGroup<InspectionFormGroupContent>({
      id: new FormControl(
        { value: inspectionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getInspection(form: InspectionFormGroup): IInspection | NewInspection {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IInspection | NewInspection;
  }

  resetForm(form: InspectionFormGroup, inspection: InspectionFormGroupInput): void {
    const inspectionRawValue = { ...this.getFormDefaults(), ...inspection };
    form.reset(
      {
        ...inspectionRawValue,
        id: { value: inspectionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InspectionFormDefaults {
    return {
      id: null,
    };
  }
}
