import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInspectorDossier } from '../inspector-dossier.model';
import { InspectorDossierService } from '../service/inspector-dossier.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './inspector-dossier-delete-dialog.component.html',
})
export class InspectorDossierDeleteDialogComponent {
  inspectorDossier?: IInspectorDossier;

  constructor(protected inspectorDossierService: InspectorDossierService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.inspectorDossierService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
