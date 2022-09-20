import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { InspectionTypeFormService, InspectionTypeFormGroup } from './inspection-type-form.service';
import { IInspectionType } from '../inspection-type.model';
import { InspectionTypeService } from '../service/inspection-type.service';

@Component({
  selector: 'jhi-inspection-type-update',
  templateUrl: './inspection-type-update.component.html',
})
export class InspectionTypeUpdateComponent implements OnInit {
  isSaving = false;
  inspectionType: IInspectionType | null = null;

  editForm: InspectionTypeFormGroup = this.inspectionTypeFormService.createInspectionTypeFormGroup();

  constructor(
    protected inspectionTypeService: InspectionTypeService,
    protected inspectionTypeFormService: InspectionTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspectionType }) => {
      this.inspectionType = inspectionType;
      if (inspectionType) {
        this.updateForm(inspectionType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inspectionType = this.inspectionTypeFormService.getInspectionType(this.editForm);
    if (inspectionType.id !== null) {
      this.subscribeToSaveResponse(this.inspectionTypeService.update(inspectionType));
    } else {
      this.subscribeToSaveResponse(this.inspectionTypeService.create(inspectionType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInspectionType>>): void {
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

  protected updateForm(inspectionType: IInspectionType): void {
    this.inspectionType = inspectionType;
    this.inspectionTypeFormService.resetForm(this.editForm, inspectionType);
  }
}
