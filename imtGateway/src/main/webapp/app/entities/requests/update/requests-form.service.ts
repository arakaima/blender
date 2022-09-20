import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRequests, NewRequests } from '../requests.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRequests for edit and NewRequestsFormGroupInput for create.
 */
type RequestsFormGroupInput = IRequests | PartialWithRequiredKeyOf<NewRequests>;

type RequestsFormDefaults = Pick<NewRequests, 'id'>;

type RequestsFormGroupContent = {
  id: FormControl<IRequests['id'] | NewRequests['id']>;
};

export type RequestsFormGroup = FormGroup<RequestsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RequestsFormService {
  createRequestsFormGroup(requests: RequestsFormGroupInput = { id: null }): RequestsFormGroup {
    const requestsRawValue = {
      ...this.getFormDefaults(),
      ...requests,
    };
    return new FormGroup<RequestsFormGroupContent>({
      id: new FormControl(
        { value: requestsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getRequests(form: RequestsFormGroup): IRequests | NewRequests {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IRequests | NewRequests;
  }

  resetForm(form: RequestsFormGroup, requests: RequestsFormGroupInput): void {
    const requestsRawValue = { ...this.getFormDefaults(), ...requests };
    form.reset(
      {
        ...requestsRawValue,
        id: { value: requestsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RequestsFormDefaults {
    return {
      id: null,
    };
  }
}
