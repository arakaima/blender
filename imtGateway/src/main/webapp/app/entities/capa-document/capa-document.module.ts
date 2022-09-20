import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CapaDocumentComponent } from './list/capa-document.component';
import { CapaDocumentDetailComponent } from './detail/capa-document-detail.component';
import { CapaDocumentUpdateComponent } from './update/capa-document-update.component';
import { CapaDocumentDeleteDialogComponent } from './delete/capa-document-delete-dialog.component';
import { CapaDocumentRoutingModule } from './route/capa-document-routing.module';

@NgModule({
  imports: [SharedModule, CapaDocumentRoutingModule],
  declarations: [CapaDocumentComponent, CapaDocumentDetailComponent, CapaDocumentUpdateComponent, CapaDocumentDeleteDialogComponent],
})
export class CapaDocumentModule {}
