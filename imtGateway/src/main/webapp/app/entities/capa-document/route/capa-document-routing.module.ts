import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CapaDocumentComponent } from '../list/capa-document.component';
import { CapaDocumentDetailComponent } from '../detail/capa-document-detail.component';
import { CapaDocumentUpdateComponent } from '../update/capa-document-update.component';
import { CapaDocumentRoutingResolveService } from './capa-document-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const capaDocumentRoute: Routes = [
  {
    path: '',
    component: CapaDocumentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CapaDocumentDetailComponent,
    resolve: {
      capaDocument: CapaDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CapaDocumentUpdateComponent,
    resolve: {
      capaDocument: CapaDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CapaDocumentUpdateComponent,
    resolve: {
      capaDocument: CapaDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(capaDocumentRoute)],
  exports: [RouterModule],
})
export class CapaDocumentRoutingModule {}
