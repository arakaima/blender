import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeficiencyComponent } from '../list/deficiency.component';
import { DeficiencyDetailComponent } from '../detail/deficiency-detail.component';
import { DeficiencyUpdateComponent } from '../update/deficiency-update.component';
import { DeficiencyRoutingResolveService } from './deficiency-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const deficiencyRoute: Routes = [
  {
    path: '',
    component: DeficiencyComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeficiencyDetailComponent,
    resolve: {
      deficiency: DeficiencyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeficiencyUpdateComponent,
    resolve: {
      deficiency: DeficiencyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeficiencyUpdateComponent,
    resolve: {
      deficiency: DeficiencyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deficiencyRoute)],
  exports: [RouterModule],
})
export class DeficiencyRoutingModule {}
