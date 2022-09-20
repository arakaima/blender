import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRiskAssessment } from '../risk-assessment.model';
import { RiskAssessmentService } from '../service/risk-assessment.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './risk-assessment-delete-dialog.component.html',
})
export class RiskAssessmentDeleteDialogComponent {
  riskAssessment?: IRiskAssessment;

  constructor(protected riskAssessmentService: RiskAssessmentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.riskAssessmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
