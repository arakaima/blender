import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AttachedDocumentFormService, AttachedDocumentFormGroup } from './attached-document-form.service';
import { IAttachedDocument } from '../attached-document.model';
import { AttachedDocumentService } from '../service/attached-document.service';

@Component({
  selector: 'jhi-attached-document-update',
  templateUrl: './attached-document-update.component.html',
})
export class AttachedDocumentUpdateComponent implements OnInit {
  isSaving = false;
  attachedDocument: IAttachedDocument | null = null;

  editForm: AttachedDocumentFormGroup = this.attachedDocumentFormService.createAttachedDocumentFormGroup();

  constructor(
    protected attachedDocumentService: AttachedDocumentService,
    protected attachedDocumentFormService: AttachedDocumentFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attachedDocument }) => {
      this.attachedDocument = attachedDocument;
      if (attachedDocument) {
        this.updateForm(attachedDocument);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attachedDocument = this.attachedDocumentFormService.getAttachedDocument(this.editForm);
    if (attachedDocument.id !== null) {
      this.subscribeToSaveResponse(this.attachedDocumentService.update(attachedDocument));
    } else {
      this.subscribeToSaveResponse(this.attachedDocumentService.create(attachedDocument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttachedDocument>>): void {
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

  protected updateForm(attachedDocument: IAttachedDocument): void {
    this.attachedDocument = attachedDocument;
    this.attachedDocumentFormService.resetForm(this.editForm, attachedDocument);
  }
}
