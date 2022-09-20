import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeficiencyComponent } from './list/deficiency.component';
import { DeficiencyDetailComponent } from './detail/deficiency-detail.component';
import { DeficiencyUpdateComponent } from './update/deficiency-update.component';
import { DeficiencyDeleteDialogComponent } from './delete/deficiency-delete-dialog.component';
import { DeficiencyRoutingModule } from './route/deficiency-routing.module';

@NgModule({
  imports: [SharedModule, DeficiencyRoutingModule],
  declarations: [DeficiencyComponent, DeficiencyDetailComponent, DeficiencyUpdateComponent, DeficiencyDeleteDialogComponent],
})
export class DeficiencyModule {}
