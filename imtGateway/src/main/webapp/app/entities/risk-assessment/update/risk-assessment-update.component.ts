import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { RiskAssessmentFormService, RiskAssessmentFormGroup } from './risk-assessment-form.service';
import { IRiskAssessment } from '../risk-assessment.model';
import { RiskAssessmentService } from '../service/risk-assessment.service';

@Component({
  selector: 'jhi-risk-assessment-update',
  templateUrl: './risk-assessment-update.component.html',
})
export class RiskAssessmentUpdateComponent implements OnInit {
  isSaving = false;
  riskAssessment: IRiskAssessment | null = null;

  editForm: RiskAssessmentFormGroup = this.riskAssessmentFormService.createRiskAssessmentFormGroup();

  constructor(
    protected riskAssessmentService: RiskAssessmentService,
    protected riskAssessmentFormService: RiskAssessmentFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ riskAssessment }) => {
      this.riskAssessment = riskAssessment;
      if (riskAssessment) {
        this.updateForm(riskAssessment);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const riskAssessment = this.riskAssessmentFormService.getRiskAssessment(this.editForm);
    if (riskAssessment.id !== null) {
      this.subscribeToSaveResponse(this.riskAssessmentService.update(riskAssessment));
    } else {
      this.subscribeToSaveResponse(this.riskAssessmentService.create(riskAssessment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRiskAssessment>>): void {
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

  protected updateForm(riskAssessment: IRiskAssessment): void {
    this.riskAssessment = riskAssessment;
    this.riskAssessmentFormService.resetForm(this.editForm, riskAssessment);
  }
}
