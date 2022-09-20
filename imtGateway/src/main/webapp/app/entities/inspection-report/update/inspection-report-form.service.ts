import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInspectionReport, NewInspectionReport } from '../inspection-report.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInspectionReport for edit and NewInspectionReportFormGroupInput for create.
 */
type InspectionReportFormGroupInput = IInspectionReport | PartialWithRequiredKeyOf<NewInspectionReport>;

type InspectionReportFormDefaults = Pick<NewInspectionReport, 'id'>;

type InspectionReportFormGroupContent = {
  id: FormControl<IInspectionReport['id'] | NewInspectionReport['id']>;
};

export type InspectionReportFormGroup = FormGroup<InspectionReportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InspectionReportFormService {
  createInspectionReportFormGroup(inspectionReport: InspectionReportFormGroupInput = { id: null }): InspectionReportFormGroup {
    const inspectionReportRawValue = {
      ...this.getFormDefaults(),
      ...inspectionReport,
    };
    return new FormGroup<InspectionReportFormGroupContent>({
      id: new FormControl(
        { value: inspectionReportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getInspectionReport(form: InspectionReportFormGroup): IInspectionReport | NewInspectionReport {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as IInspectionReport | NewInspectionReport;
  }

  resetForm(form: InspectionReportFormGroup, inspectionReport: InspectionReportFormGroupInput): void {
    const inspectionReportRawValue = { ...this.getFormDefaults(), ...inspectionReport };
    form.reset(
      {
        ...inspectionReportRawValue,
        id: { value: inspectionReportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InspectionReportFormDefaults {
    return {
      id: null,
    };
  }
}
