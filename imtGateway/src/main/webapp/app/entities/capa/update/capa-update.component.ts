import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CapaFormService, CapaFormGroup } from './capa-form.service';
import { ICapa } from '../capa.model';
import { CapaService } from '../service/capa.service';

@Component({
  selector: 'jhi-capa-update',
  templateUrl: './capa-update.component.html',
})
export class CapaUpdateComponent implements OnInit {
  isSaving = false;
  capa: ICapa | null = null;

  editForm: CapaFormGroup = this.capaFormService.createCapaFormGroup();

  constructor(protected capaService: CapaService, protected capaFormService: CapaFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ capa }) => {
      this.capa = capa;
      if (capa) {
        this.updateForm(capa);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const capa = this.capaFormService.getCapa(this.editForm);
    if (capa.id !== null) {
      this.subscribeToSaveResponse(this.capaService.update(capa));
    } else {
      this.subscribeToSaveResponse(this.capaService.create(capa));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICapa>>): void {
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

  protected updateForm(capa: ICapa): void {
    this.capa = capa;
    this.capaFormService.resetForm(this.editForm, capa);
  }
}
