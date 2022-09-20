import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { OrganizationDocumentFormService, OrganizationDocumentFormGroup } from './organization-document-form.service';
import { IOrganizationDocument } from '../organization-document.model';
import { OrganizationDocumentService } from '../service/organization-document.service';

@Component({
  selector: 'jhi-organization-document-update',
  templateUrl: './organization-document-update.component.html',
})
export class OrganizationDocumentUpdateComponent implements OnInit {
  isSaving = false;
  organizationDocument: IOrganizationDocument | null = null;

  editForm: OrganizationDocumentFormGroup = this.organizationDocumentFormService.createOrganizationDocumentFormGroup();

  constructor(
    protected organizationDocumentService: OrganizationDocumentService,
    protected organizationDocumentFormService: OrganizationDocumentFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationDocument }) => {
      this.organizationDocument = organizationDocument;
      if (organizationDocument) {
        this.updateForm(organizationDocument);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organizationDocument = this.organizationDocumentFormService.getOrganizationDocument(this.editForm);
    if (organizationDocument.id !== null) {
      this.subscribeToSaveResponse(this.organizationDocumentService.update(organizationDocument));
    } else {
      this.subscribeToSaveResponse(this.organizationDocumentService.create(organizationDocument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganizationDocument>>): void {
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

  protected updateForm(organizationDocument: IOrganizationDocument): void {
    this.organizationDocument = organizationDocument;
    this.organizationDocumentFormService.resetForm(this.editForm, organizationDocument);
  }
}
