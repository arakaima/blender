import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InspectionTypeComponent } from '../list/inspection-type.component';
import { InspectionTypeDetailComponent } from '../detail/inspection-type-detail.component';
import { InspectionTypeUpdateComponent } from '../update/inspection-type-update.component';
import { InspectionTypeRoutingResolveService } from './inspection-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const inspectionTypeRoute: Routes = [
  {
    path: '',
    component: InspectionTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InspectionTypeDetailComponent,
    resolve: {
      inspectionType: InspectionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InspectionTypeUpdateComponent,
    resolve: {
      inspectionType: InspectionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InspectionTypeUpdateComponent,
    resolve: {
      inspectionType: InspectionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(inspectionTypeRoute)],
  exports: [RouterModule],
})
export class InspectionTypeRoutingModule {}
