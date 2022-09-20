import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InspectorDossierComponent } from '../list/inspector-dossier.component';
import { InspectorDossierDetailComponent } from '../detail/inspector-dossier-detail.component';
import { InspectorDossierUpdateComponent } from '../update/inspector-dossier-update.component';
import { InspectorDossierRoutingResolveService } from './inspector-dossier-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const inspectorDossierRoute: Routes = [
  {
    path: '',
    component: InspectorDossierComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InspectorDossierDetailComponent,
    resolve: {
      inspectorDossier: InspectorDossierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InspectorDossierUpdateComponent,
    resolve: {
      inspectorDossier: InspectorDossierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InspectorDossierUpdateComponent,
    resolve: {
      inspectorDossier: InspectorDossierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(inspectorDossierRoute)],
  exports: [RouterModule],
})
export class InspectorDossierRoutingModule {}
