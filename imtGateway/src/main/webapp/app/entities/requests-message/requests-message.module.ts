import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RequestsMessageComponent } from './list/requests-message.component';
import { RequestsMessageDetailComponent } from './detail/requests-message-detail.component';
import { RequestsMessageUpdateComponent } from './update/requests-message-update.component';
import { RequestsMessageDeleteDialogComponent } from './delete/requests-message-delete-dialog.component';
import { RequestsMessageRoutingModule } from './route/requests-message-routing.module';

@NgModule({
  imports: [SharedModule, RequestsMessageRoutingModule],
  declarations: [
    RequestsMessageComponent,
    RequestsMessageDetailComponent,
    RequestsMessageUpdateComponent,
    RequestsMessageDeleteDialogComponent,
  ],
})
export class RequestsMessageModule {}
