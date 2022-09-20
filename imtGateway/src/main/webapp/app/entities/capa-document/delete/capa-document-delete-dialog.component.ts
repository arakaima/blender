import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICapaDocument } from '../capa-document.model';
import { CapaDocumentService } from '../service/capa-document.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './capa-document-delete-dialog.component.html',
})
export class CapaDocumentDeleteDialogComponent {
  capaDocument?: ICapaDocument;

  constructor(protected capaDocumentService: CapaDocumentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.capaDocumentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
