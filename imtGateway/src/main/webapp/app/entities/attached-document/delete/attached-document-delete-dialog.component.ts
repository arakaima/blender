import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttachedDocument } from '../attached-document.model';
import { AttachedDocumentService } from '../service/attached-document.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './attached-document-delete-dialog.component.html',
})
export class AttachedDocumentDeleteDialogComponent {
  attachedDocument?: IAttachedDocument;

  constructor(protected attachedDocumentService: AttachedDocumentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.attachedDocumentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
