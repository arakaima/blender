import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RequestsComponent } from './list/requests.component';
import { RequestsDetailComponent } from './detail/requests-detail.component';
import { RequestsUpdateComponent } from './update/requests-update.component';
import { RequestsDeleteDialogComponent } from './delete/requests-delete-dialog.component';
import { RequestsRoutingModule } from './route/requests-routing.module';

@NgModule({
  imports: [SharedModule, RequestsRoutingModule],
  declarations: [RequestsComponent, RequestsDetailComponent, RequestsUpdateComponent, RequestsDeleteDialogComponent],
})
export class RequestsModule {}
