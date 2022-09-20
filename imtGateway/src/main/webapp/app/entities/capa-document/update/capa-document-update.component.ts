import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CapaDocumentFormService, CapaDocumentFormGroup } from './capa-document-form.service';
import { ICapaDocument } from '../capa-document.model';
import { CapaDocumentService } from '../service/capa-document.service';

@Component({
  selector: 'jhi-capa-document-update',
  templateUrl: './capa-document-update.component.html',
})
export class CapaDocumentUpdateComponent implements OnInit {
  isSaving = false;
  capaDocument: ICapaDocument | null = null;

  editForm: CapaDocumentFormGroup = this.capaDocumentFormService.createCapaDocumentFormGroup();

  constructor(
    protected capaDocumentService: CapaDocumentService,
    protected capaDocumentFormService: CapaDocumentFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ capaDocument }) => {
      this.capaDocument = capaDocument;
      if (capaDocument) {
        this.updateForm(capaDocument);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const capaDocument = this.capaDocumentFormService.getCapaDocument(this.editForm);
    if (capaDocument.id !== null) {
      this.subscribeToSaveResponse(this.capaDocumentService.update(capaDocument));
    } else {
      this.subscribeToSaveResponse(this.capaDocumentService.create(capaDocument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICapaDocument>>): void {
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

  protected updateForm(capaDocument: ICapaDocument): void {
    this.capaDocument = capaDocument;
    this.capaDocumentFormService.resetForm(this.editForm, capaDocument);
  }
}
