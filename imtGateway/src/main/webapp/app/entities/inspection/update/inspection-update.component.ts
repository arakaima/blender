import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { InspectionFormService, InspectionFormGroup } from './inspection-form.service';
import { IInspection } from '../inspection.model';
import { InspectionService } from '../service/inspection.service';

@Component({
  selector: 'jhi-inspection-update',
  templateUrl: './inspection-update.component.html',
})
export class InspectionUpdateComponent implements OnInit {
  isSaving = false;
  inspection: IInspection | null = null;

  editForm: InspectionFormGroup = this.inspectionFormService.createInspectionFormGroup();

  constructor(
    protected inspectionService: InspectionService,
    protected inspectionFormService: InspectionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspection }) => {
      this.inspection = inspection;
      if (inspection) {
        this.updateForm(inspection);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inspection = this.inspectionFormService.getInspection(this.editForm);
    if (inspection.id !== null) {
      this.subscribeToSaveResponse(this.inspectionService.update(inspection));
    } else {
      this.subscribeToSaveResponse(this.inspectionService.create(inspection));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInspection>>): void {
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

  protected updateForm(inspection: IInspection): void {
    this.inspection = inspection;
    this.inspectionFormService.resetForm(this.editForm, inspection);
  }
}
