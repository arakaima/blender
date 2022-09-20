import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AttachedDocumentComponent } from '../list/attached-document.component';
import { AttachedDocumentDetailComponent } from '../detail/attached-document-detail.component';
import { AttachedDocumentUpdateComponent } from '../update/attached-document-update.component';
import { AttachedDocumentRoutingResolveService } from './attached-document-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const attachedDocumentRoute: Routes = [
  {
    path: '',
    component: AttachedDocumentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttachedDocumentDetailComponent,
    resolve: {
      attachedDocument: AttachedDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AttachedDocumentUpdateComponent,
    resolve: {
      attachedDocument: AttachedDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AttachedDocumentUpdateComponent,
    resolve: {
      attachedDocument: AttachedDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(attachedDocumentRoute)],
  exports: [RouterModule],
})
export class AttachedDocumentRoutingModule {}
