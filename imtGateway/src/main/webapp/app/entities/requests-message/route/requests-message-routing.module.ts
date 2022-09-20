import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RequestsMessageComponent } from '../list/requests-message.component';
import { RequestsMessageDetailComponent } from '../detail/requests-message-detail.component';
import { RequestsMessageUpdateComponent } from '../update/requests-message-update.component';
import { RequestsMessageRoutingResolveService } from './requests-message-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const requestsMessageRoute: Routes = [
  {
    path: '',
    component: RequestsMessageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RequestsMessageDetailComponent,
    resolve: {
      requestsMessage: RequestsMessageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RequestsMessageUpdateComponent,
    resolve: {
      requestsMessage: RequestsMessageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RequestsMessageUpdateComponent,
    resolve: {
      requestsMessage: RequestsMessageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(requestsMessageRoute)],
  exports: [RouterModule],
})
export class RequestsMessageRoutingModule {}
