import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrganizationDocument } from '../organization-document.model';

@Component({
  selector: 'jhi-organization-document-detail',
  templateUrl: './organization-document-detail.component.html',
})
export class OrganizationDocumentDetailComponent implements OnInit {
  organizationDocument: IOrganizationDocument | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationDocument }) => {
      this.organizationDocument = organizationDocument;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
