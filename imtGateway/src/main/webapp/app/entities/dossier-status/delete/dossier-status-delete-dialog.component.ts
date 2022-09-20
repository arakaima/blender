import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDossierStatus } from '../dossier-status.model';
import { DossierStatusService } from '../service/dossier-status.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './dossier-status-delete-dialog.component.html',
})
export class DossierStatusDeleteDialogComponent {
  dossierStatus?: IDossierStatus;

  constructor(protected dossierStatusService: DossierStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.dossierStatusService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
