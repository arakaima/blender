import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DossierTypeFormService, DossierTypeFormGroup } from './dossier-type-form.service';
import { IDossierType } from '../dossier-type.model';
import { DossierTypeService } from '../service/dossier-type.service';

@Component({
  selector: 'jhi-dossier-type-update',
  templateUrl: './dossier-type-update.component.html',
})
export class DossierTypeUpdateComponent implements OnInit {
  isSaving = false;
  dossierType: IDossierType | null = null;

  editForm: DossierTypeFormGroup = this.dossierTypeFormService.createDossierTypeFormGroup();

  constructor(
    protected dossierTypeService: DossierTypeService,
    protected dossierTypeFormService: DossierTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dossierType }) => {
      this.dossierType = dossierType;
      if (dossierType) {
        this.updateForm(dossierType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dossierType = this.dossierTypeFormService.getDossierType(this.editForm);
    if (dossierType.id !== null) {
      this.subscribeToSaveResponse(this.dossierTypeService.update(dossierType));
    } else {
      this.subscribeToSaveResponse(this.dossierTypeService.create(dossierType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDossierType>>): void {
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

  protected updateForm(dossierType: IDossierType): void {
    this.dossierType = dossierType;
    this.dossierTypeFormService.resetForm(this.editForm, dossierType);
  }
}
