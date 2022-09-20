import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRequestsMessage } from '../requests-message.model';
import { RequestsMessageService } from '../service/requests-message.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './requests-message-delete-dialog.component.html',
})
export class RequestsMessageDeleteDialogComponent {
  requestsMessage?: IRequestsMessage;

  constructor(protected requestsMessageService: RequestsMessageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.requestsMessageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
