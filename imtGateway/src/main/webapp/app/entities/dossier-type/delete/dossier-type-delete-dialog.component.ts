import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDossierType } from '../dossier-type.model';
import { DossierTypeService } from '../service/dossier-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './dossier-type-delete-dialog.component.html',
})
export class DossierTypeDeleteDialogComponent {
  dossierType?: IDossierType;

  constructor(protected dossierTypeService: DossierTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.dossierTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
