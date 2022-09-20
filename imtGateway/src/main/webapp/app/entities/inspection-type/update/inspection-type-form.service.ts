import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInspectionType, NewInspectionType } from '../inspection-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInspectionType for edit and NewInspectionTypeFormGroupInput for create.
 */
type InspectionTypeFormGroupInput = IInspectionType | PartialWithRequiredKeyOf<NewInspectionType>;

type InspectionTypeFormDefaults = Pick<NewInspectionType, 'id'>;

type InspectionTypeFormGroupContent = {
  id: FormControl<IInspectionType['id'] | NewInspectionType['id']>;
};

export type InspectionTypeFormGroup = FormGroup<InspectionTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InspectionTypeFormService {
  createInspectionTypeFormGroup(inspectionType: InspectionTypeFormGroupInput = { id: null }): InspectionTypeFormGroup {
    const inspectionTypeRawValue = {
      ...this.getFormDefaults(),
      ...inspectionType,
    };
    return new FormGroup<InspectionTypeFormGroupContent>({
      id: new FormControl(
        { value: inspectionTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getInspectionType(form: InspectionTypeFormGroup): IInspectionType | NewInspectionType {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IInspectionType | NewInspectionType;
  }

  resetForm(form: InspectionTypeFormGroup, inspectionType: InspectionTypeFormGroupInput): void {
    const inspectionTypeRawValue = { ...this.getFormDefaults(), ...inspectionType };
    form.reset(
      {
        ...inspectionTypeRawValue,
        id: { value: inspectionTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InspectionTypeFormDefaults {
    return {
      id: null,
    };
  }
}
