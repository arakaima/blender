import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RiskAssessmentComponent } from '../list/risk-assessment.component';
import { RiskAssessmentDetailComponent } from '../detail/risk-assessment-detail.component';
import { RiskAssessmentUpdateComponent } from '../update/risk-assessment-update.component';
import { RiskAssessmentRoutingResolveService } from './risk-assessment-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const riskAssessmentRoute: Routes = [
  {
    path: '',
    component: RiskAssessmentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RiskAssessmentDetailComponent,
    resolve: {
      riskAssessment: RiskAssessmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RiskAssessmentUpdateComponent,
    resolve: {
      riskAssessment: RiskAssessmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RiskAssessmentUpdateComponent,
    resolve: {
      riskAssessment: RiskAssessmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(riskAssessmentRoute)],
  exports: [RouterModule],
})
export class RiskAssessmentRoutingModule {}
