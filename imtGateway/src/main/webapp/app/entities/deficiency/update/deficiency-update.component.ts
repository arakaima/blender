import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DeficiencyFormService, DeficiencyFormGroup } from './deficiency-form.service';
import { IDeficiency } from '../deficiency.model';
import { DeficiencyService } from '../service/deficiency.service';

@Component({
  selector: 'jhi-deficiency-update',
  templateUrl: './deficiency-update.component.html',
})
export class DeficiencyUpdateComponent implements OnInit {
  isSaving = false;
  deficiency: IDeficiency | null = null;

  editForm: DeficiencyFormGroup = this.deficiencyFormService.createDeficiencyFormGroup();

  constructor(
    protected deficiencyService: DeficiencyService,
    protected deficiencyFormService: DeficiencyFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deficiency }) => {
      this.deficiency = deficiency;
      if (deficiency) {
        this.updateForm(deficiency);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deficiency = this.deficiencyFormService.getDeficiency(this.editForm);
    if (deficiency.id !== null) {
      this.subscribeToSaveResponse(this.deficiencyService.update(deficiency));
    } else {
      this.subscribeToSaveResponse(this.deficiencyService.create(deficiency));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeficiency>>): void {
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

  protected updateForm(deficiency: IDeficiency): void {
    this.deficiency = deficiency;
    this.deficiencyFormService.resetForm(this.editForm, deficiency);
  }
}
