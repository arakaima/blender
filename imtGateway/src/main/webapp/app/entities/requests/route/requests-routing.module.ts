import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RequestsComponent } from '../list/requests.component';
import { RequestsDetailComponent } from '../detail/requests-detail.component';
import { RequestsUpdateComponent } from '../update/requests-update.component';
import { RequestsRoutingResolveService } from './requests-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const requestsRoute: Routes = [
  {
    path: '',
    component: RequestsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RequestsDetailComponent,
    resolve: {
      requests: RequestsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RequestsUpdateComponent,
    resolve: {
      requests: RequestsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RequestsUpdateComponent,
    resolve: {
      requests: RequestsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(requestsRoute)],
  exports: [RouterModule],
})
export class RequestsRoutingModule {}
