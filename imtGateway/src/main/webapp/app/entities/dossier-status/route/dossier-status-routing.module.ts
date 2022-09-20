import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DossierStatusComponent } from '../list/dossier-status.component';
import { DossierStatusDetailComponent } from '../detail/dossier-status-detail.component';
import { DossierStatusUpdateComponent } from '../update/dossier-status-update.component';
import { DossierStatusRoutingResolveService } from './dossier-status-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const dossierStatusRoute: Routes = [
  {
    path: '',
    component: DossierStatusComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DossierStatusDetailComponent,
    resolve: {
      dossierStatus: DossierStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DossierStatusUpdateComponent,
    resolve: {
      dossierStatus: DossierStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DossierStatusUpdateComponent,
    resolve: {
      dossierStatus: DossierStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dossierStatusRoute)],
  exports: [RouterModule],
})
export class DossierStatusRoutingModule {}
