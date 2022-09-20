import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InspectionReportComponent } from './list/inspection-report.component';
import { InspectionReportDetailComponent } from './detail/inspection-report-detail.component';
import { InspectionReportUpdateComponent } from './update/inspection-report-update.component';
import { InspectionReportDeleteDialogComponent } from './delete/inspection-report-delete-dialog.component';
import { InspectionReportRoutingModule } from './route/inspection-report-routing.module';

@NgModule({
  imports: [SharedModule, InspectionReportRoutingModule],
  declarations: [
    InspectionReportComponent,
    InspectionReportDetailComponent,
    InspectionReportUpdateComponent,
    InspectionReportDeleteDialogComponent,
  ],
})
export class InspectionReportModule {}
