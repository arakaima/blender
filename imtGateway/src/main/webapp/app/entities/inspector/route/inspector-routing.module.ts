import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InspectorComponent } from '../list/inspector.component';
import { InspectorDetailComponent } from '../detail/inspector-detail.component';
import { InspectorUpdateComponent } from '../update/inspector-update.component';
import { InspectorRoutingResolveService } from './inspector-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const inspectorRoute: Routes = [
  {
    path: '',
    component: InspectorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InspectorDetailComponent,
    resolve: {
      inspector: InspectorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InspectorUpdateComponent,
    resolve: {
      inspector: InspectorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InspectorUpdateComponent,
    resolve: {
      inspector: InspectorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(inspectorRoute)],
  exports: [RouterModule],
})
export class InspectorRoutingModule {}
