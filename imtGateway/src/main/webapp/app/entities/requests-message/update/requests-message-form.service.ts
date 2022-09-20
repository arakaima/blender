import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRequestsMessage, NewRequestsMessage } from '../requests-message.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRequestsMessage for edit and NewRequestsMessageFormGroupInput for create.
 */
type RequestsMessageFormGroupInput = IRequestsMessage | PartialWithRequiredKeyOf<NewRequestsMessage>;

type RequestsMessageFormDefaults = Pick<NewRequestsMessage, 'id'>;

type RequestsMessageFormGroupContent = {
  id: FormControl<IRequestsMessage['id'] | NewRequestsMessage['id']>;
};

export type RequestsMessageFormGroup = FormGroup<RequestsMessageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RequestsMessageFormService {
  createRequestsMessageFormGroup(requestsMessage: RequestsMessageFormGroupInput = { id: null }): RequestsMessageFormGroup {
    const requestsMessageRawValue = {
      ...this.getFormDefaults(),
      ...requestsMessage,
    };
    return new FormGroup<RequestsMessageFormGroupContent>({
      id: new FormControl(
        { value: requestsMessageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getRequestsMessage(form: RequestsMessageFormGroup): IRequestsMessage | NewRequestsMessage {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IRequestsMessage | NewRequestsMessage;
  }

  resetForm(form: RequestsMessageFormGroup, requestsMessage: RequestsMessageFormGroupInput): void {
    const requestsMessageRawValue = { ...this.getFormDefaults(), ...requestsMessage };
    form.reset(
      {
        ...requestsMessageRawValue,
        id: { value: requestsMessageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RequestsMessageFormDefaults {
    return {
      id: null,
    };
  }
}
