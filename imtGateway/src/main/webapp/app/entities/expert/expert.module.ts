import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExpertComponent } from './list/expert.component';
import { ExpertDetailComponent } from './detail/expert-detail.component';
import { ExpertUpdateComponent } from './update/expert-update.component';
import { ExpertDeleteDialogComponent } from './delete/expert-delete-dialog.component';
import { ExpertRoutingModule } from './route/expert-routing.module';

@NgModule({
  imports: [SharedModule, ExpertRoutingModule],
  declarations: [ExpertComponent, ExpertDetailComponent, ExpertUpdateComponent, ExpertDeleteDialogComponent],
})
export class ExpertModule {}
