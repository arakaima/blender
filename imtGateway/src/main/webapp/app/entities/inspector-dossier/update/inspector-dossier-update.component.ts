import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { InspectorDossierFormService, InspectorDossierFormGroup } from './inspector-dossier-form.service';
import { IInspectorDossier } from '../inspector-dossier.model';
import { InspectorDossierService } from '../service/inspector-dossier.service';

@Component({
  selector: 'jhi-inspector-dossier-update',
  templateUrl: './inspector-dossier-update.component.html',
})
export class InspectorDossierUpdateComponent implements OnInit {
  isSaving = false;
  inspectorDossier: IInspectorDossier | null = null;

  editForm: InspectorDossierFormGroup = this.inspectorDossierFormService.createInspectorDossierFormGroup();

  constructor(
    protected inspectorDossierService: InspectorDossierService,
    protected inspectorDossierFormService: InspectorDossierFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspectorDossier }) => {
      this.inspectorDossier = inspectorDossier;
      if (inspectorDossier) {
        this.updateForm(inspectorDossier);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inspectorDossier = this.inspectorDossierFormService.getInspectorDossier(this.editForm);
    if (inspectorDossier.id !== null) {
      this.subscribeToSaveResponse(this.inspectorDossierService.update(inspectorDossier));
    } else {
      this.subscribeToSaveResponse(this.inspectorDossierService.create(inspectorDossier));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInspectorDossier>>): void {
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

  protected updateForm(inspectorDossier: IInspectorDossier): void {
    this.inspectorDossier = inspectorDossier;
    this.inspectorDossierFormService.resetForm(this.editForm, inspectorDossier);
  }
}
