import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInspector, NewInspector } from '../inspector.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInspector for edit and NewInspectorFormGroupInput for create.
 */
type InspectorFormGroupInput = IInspector | PartialWithRequiredKeyOf<NewInspector>;

type InspectorFormDefaults = Pick<NewInspector, 'id'>;

type InspectorFormGroupContent = {
  id: FormControl<IInspector['id'] | NewInspector['id']>;
};

export type InspectorFormGroup = FormGroup<InspectorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InspectorFormService {
  createInspectorFormGroup(inspector: InspectorFormGroupInput = { id: null }): InspectorFormGroup {
    const inspectorRawValue = {
      ...this.getFormDefaults(),
      ...inspector,
    };
    return new FormGroup<InspectorFormGroupContent>({
      id: new FormControl(
        { value: inspectorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getInspector(form: InspectorFormGroup): IInspector | NewInspector {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IInspector | NewInspector;
  }

  resetForm(form: InspectorFormGroup, inspector: InspectorFormGroupInput): void {
    const inspectorRawValue = { ...this.getFormDefaults(), ...inspector };
    form.reset(
      {
        ...inspectorRawValue,
        id: { value: inspectorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InspectorFormDefaults {
    return {
      id: null,
    };
  }
}
