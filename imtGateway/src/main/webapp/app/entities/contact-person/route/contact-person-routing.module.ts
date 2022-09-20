import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactPersonComponent } from '../list/contact-person.component';
import { ContactPersonDetailComponent } from '../detail/contact-person-detail.component';
import { ContactPersonUpdateComponent } from '../update/contact-person-update.component';
import { ContactPersonRoutingResolveService } from './contact-person-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const contactPersonRoute: Routes = [
  {
    path: '',
    component: ContactPersonComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactPersonDetailComponent,
    resolve: {
      contactPerson: ContactPersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactPersonUpdateComponent,
    resolve: {
      contactPerson: ContactPersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactPersonUpdateComponent,
    resolve: {
      contactPerson: ContactPersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactPersonRoute)],
  exports: [RouterModule],
})
export class ContactPersonRoutingModule {}
