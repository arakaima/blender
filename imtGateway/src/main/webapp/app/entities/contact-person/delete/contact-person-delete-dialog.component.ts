import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactPerson } from '../contact-person.model';
import { ContactPersonService } from '../service/contact-person.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './contact-person-delete-dialog.component.html',
})
export class ContactPersonDeleteDialogComponent {
  contactPerson?: IContactPerson;

  constructor(protected contactPersonService: ContactPersonService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.contactPersonService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
