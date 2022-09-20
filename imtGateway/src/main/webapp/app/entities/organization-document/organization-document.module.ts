import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrganizationDocumentComponent } from './list/organization-document.component';
import { OrganizationDocumentDetailComponent } from './detail/organization-document-detail.component';
import { OrganizationDocumentUpdateComponent } from './update/organization-document-update.component';
import { OrganizationDocumentDeleteDialogComponent } from './delete/organization-document-delete-dialog.component';
import { OrganizationDocumentRoutingModule } from './route/organization-document-routing.module';

@NgModule({
  imports: [SharedModule, OrganizationDocumentRoutingModule],
  declarations: [
    OrganizationDocumentComponent,
    OrganizationDocumentDetailComponent,
    OrganizationDocumentUpdateComponent,
    OrganizationDocumentDeleteDialogComponent,
  ],
})
export class OrganizationDocumentModule {}
