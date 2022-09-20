import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DossierTypeComponent } from '../list/dossier-type.component';
import { DossierTypeDetailComponent } from '../detail/dossier-type-detail.component';
import { DossierTypeUpdateComponent } from '../update/dossier-type-update.component';
import { DossierTypeRoutingResolveService } from './dossier-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const dossierTypeRoute: Routes = [
  {
    path: '',
    component: DossierTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DossierTypeDetailComponent,
    resolve: {
      dossierType: DossierTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DossierTypeUpdateComponent,
    resolve: {
      dossierType: DossierTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DossierTypeUpdateComponent,
    resolve: {
      dossierType: DossierTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dossierTypeRoute)],
  exports: [RouterModule],
})
export class DossierTypeRoutingModule {}
