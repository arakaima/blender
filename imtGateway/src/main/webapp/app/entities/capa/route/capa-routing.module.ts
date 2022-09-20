import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CapaComponent } from '../list/capa.component';
import { CapaDetailComponent } from '../detail/capa-detail.component';
import { CapaUpdateComponent } from '../update/capa-update.component';
import { CapaRoutingResolveService } from './capa-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const capaRoute: Routes = [
  {
    path: '',
    component: CapaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CapaDetailComponent,
    resolve: {
      capa: CapaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CapaUpdateComponent,
    resolve: {
      capa: CapaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CapaUpdateComponent,
    resolve: {
      capa: CapaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(capaRoute)],
  exports: [RouterModule],
})
export class CapaRoutingModule {}
