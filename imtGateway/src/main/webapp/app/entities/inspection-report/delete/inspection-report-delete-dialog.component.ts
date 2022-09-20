import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInspectionReport } from '../inspection-report.model';
import { InspectionReportService } from '../service/inspection-report.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './inspection-report-delete-dialog.component.html',
})
export class InspectionReportDeleteDialogComponent {
  inspectionReport?: IInspectionReport;

  constructor(protected inspectionReportService: InspectionReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.inspectionReportService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
