import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrganizationDocumentComponent } from '../list/organization-document.component';
import { OrganizationDocumentDetailComponent } from '../detail/organization-document-detail.component';
import { OrganizationDocumentUpdateComponent } from '../update/organization-document-update.component';
import { OrganizationDocumentRoutingResolveService } from './organization-document-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const organizationDocumentRoute: Routes = [
  {
    path: '',
    component: OrganizationDocumentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrganizationDocumentDetailComponent,
    resolve: {
      organizationDocument: OrganizationDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrganizationDocumentUpdateComponent,
    resolve: {
      organizationDocument: OrganizationDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrganizationDocumentUpdateComponent,
    resolve: {
      organizationDocument: OrganizationDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(organizationDocumentRoute)],
  exports: [RouterModule],
})
export class OrganizationDocumentRoutingModule {}
