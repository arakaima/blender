import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AttachedDocumentComponent } from './list/attached-document.component';
import { AttachedDocumentDetailComponent } from './detail/attached-document-detail.component';
import { AttachedDocumentUpdateComponent } from './update/attached-document-update.component';
import { AttachedDocumentDeleteDialogComponent } from './delete/attached-document-delete-dialog.component';
import { AttachedDocumentRoutingModule } from './route/attached-document-routing.module';

@NgModule({
  imports: [SharedModule, AttachedDocumentRoutingModule],
  declarations: [
    AttachedDocumentComponent,
    AttachedDocumentDetailComponent,
    AttachedDocumentUpdateComponent,
    AttachedDocumentDeleteDialogComponent,
  ],
})
export class AttachedDocumentModule {}
