import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RiskAssessmentComponent } from './list/risk-assessment.component';
import { RiskAssessmentDetailComponent } from './detail/risk-assessment-detail.component';
import { RiskAssessmentUpdateComponent } from './update/risk-assessment-update.component';
import { RiskAssessmentDeleteDialogComponent } from './delete/risk-assessment-delete-dialog.component';
import { RiskAssessmentRoutingModule } from './route/risk-assessment-routing.module';

@NgModule({
  imports: [SharedModule, RiskAssessmentRoutingModule],
  declarations: [
    RiskAssessmentComponent,
    RiskAssessmentDetailComponent,
    RiskAssessmentUpdateComponent,
    RiskAssessmentDeleteDialogComponent,
  ],
})
export class RiskAssessmentModule {}
