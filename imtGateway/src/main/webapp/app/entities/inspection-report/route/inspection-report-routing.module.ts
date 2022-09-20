import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InspectionReportComponent } from '../list/inspection-report.component';
import { InspectionReportDetailComponent } from '../detail/inspection-report-detail.component';
import { InspectionReportUpdateComponent } from '../update/inspection-report-update.component';
import { InspectionReportRoutingResolveService } from './inspection-report-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const inspectionReportRoute: Routes = [
  {
    path: '',
    component: InspectionReportComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InspectionReportDetailComponent,
    resolve: {
      inspectionReport: InspectionReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InspectionReportUpdateComponent,
    resolve: {
      inspectionReport: InspectionReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InspectionReportUpdateComponent,
    resolve: {
      inspectionReport: InspectionReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(inspectionReportRoute)],
  exports: [RouterModule],
})
export class InspectionReportRoutingModule {}
