import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ExpertFormService, ExpertFormGroup } from './expert-form.service';
import { IExpert } from '../expert.model';
import { ExpertService } from '../service/expert.service';

@Component({
  selector: 'jhi-expert-update',
  templateUrl: './expert-update.component.html',
})
export class ExpertUpdateComponent implements OnInit {
  isSaving = false;
  expert: IExpert | null = null;

  editForm: ExpertFormGroup = this.expertFormService.createExpertFormGroup();

  constructor(
    protected expertService: ExpertService,
    protected expertFormService: ExpertFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expert }) => {
      this.expert = expert;
      if (expert) {
        this.updateForm(expert);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expert = this.expertFormService.getExpert(this.editForm);
    if (expert.id !== null) {
      this.subscribeToSaveResponse(this.expertService.update(expert));
    } else {
      this.subscribeToSaveResponse(this.expertService.create(expert));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpert>>): void {
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

  protected updateForm(expert: IExpert): void {
    this.expert = expert;
    this.expertFormService.resetForm(this.editForm, expert);
  }
}
