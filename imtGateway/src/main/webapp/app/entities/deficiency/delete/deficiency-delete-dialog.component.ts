import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeficiency } from '../deficiency.model';
import { DeficiencyService } from '../service/deficiency.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './deficiency-delete-dialog.component.html',
})
export class DeficiencyDeleteDialogComponent {
  deficiency?: IDeficiency;

  constructor(protected deficiencyService: DeficiencyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.deficiencyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
