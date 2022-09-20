import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DossierStatusFormService, DossierStatusFormGroup } from './dossier-status-form.service';
import { IDossierStatus } from '../dossier-status.model';
import { DossierStatusService } from '../service/dossier-status.service';

@Component({
  selector: 'jhi-dossier-status-update',
  templateUrl: './dossier-status-update.component.html',
})
export class DossierStatusUpdateComponent implements OnInit {
  isSaving = false;
  dossierStatus: IDossierStatus | null = null;

  editForm: DossierStatusFormGroup = this.dossierStatusFormService.createDossierStatusFormGroup();

  constructor(
    protected dossierStatusService: DossierStatusService,
    protected dossierStatusFormService: DossierStatusFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dossierStatus }) => {
      this.dossierStatus = dossierStatus;
      if (dossierStatus) {
        this.updateForm(dossierStatus);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dossierStatus = this.dossierStatusFormService.getDossierStatus(this.editForm);
    if (dossierStatus.id !== null) {
      this.subscribeToSaveResponse(this.dossierStatusService.update(dossierStatus));
    } else {
      this.subscribeToSaveResponse(this.dossierStatusService.create(dossierStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDossierStatus>>): void {
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

  protected updateForm(dossierStatus: IDossierStatus): void {
    this.dossierStatus = dossierStatus;
    this.dossierStatusFormService.resetForm(this.editForm, dossierStatus);
  }
}
