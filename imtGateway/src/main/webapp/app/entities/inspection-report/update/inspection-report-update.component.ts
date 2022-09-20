import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { InspectionReportFormService, InspectionReportFormGroup } from './inspection-report-form.service';
import { IInspectionReport } from '../inspection-report.model';
import { InspectionReportService } from '../service/inspection-report.service';

@Component({
  selector: 'jhi-inspection-report-update',
  templateUrl: './inspection-report-update.component.html',
})
export class InspectionReportUpdateComponent implements OnInit {
  isSaving = false;
  inspectionReport: IInspectionReport | null = null;

  editForm: InspectionReportFormGroup = this.inspectionReportFormService.createInspectionReportFormGroup();

  constructor(
    protected inspectionReportService: InspectionReportService,
    protected inspectionReportFormService: InspectionReportFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspectionReport }) => {
      this.inspectionReport = inspectionReport;
      if (inspectionReport) {
        this.updateForm(inspectionReport);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inspectionReport = this.inspectionReportFormService.getInspectionReport(this.editForm);
    if (inspectionReport.id !== null) {
      this.subscribeToSaveResponse(this.inspectionReportService.update(inspectionReport));
    } else {
      this.subscribeToSaveResponse(this.inspectionReportService.create(inspectionReport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInspectionReport>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(inspectionReport: IInspectionReport): void {
    this.inspectionReport = inspectionReport;
    this.inspectionReportFormService.resetForm(this.editForm, inspectionReport);
  }
}
