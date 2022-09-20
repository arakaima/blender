import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrganizationDocument } from '../organization-document.model';
import { OrganizationDocumentService } from '../service/organization-document.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './organization-document-delete-dialog.component.html',
})
export class OrganizationDocumentDeleteDialogComponent {
  organizationDocument?: IOrganizationDocument;

  constructor(protected organizationDocumentService: OrganizationDocumentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.organizationDocumentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
