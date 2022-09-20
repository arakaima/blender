import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DossierStatusComponent } from './list/dossier-status.component';
import { DossierStatusDetailComponent } from './detail/dossier-status-detail.component';
import { DossierStatusUpdateComponent } from './update/dossier-status-update.component';
import { DossierStatusDeleteDialogComponent } from './delete/dossier-status-delete-dialog.component';
import { DossierStatusRoutingModule } from './route/dossier-status-routing.module';

@NgModule({
  imports: [SharedModule, DossierStatusRoutingModule],
  declarations: [DossierStatusComponent, DossierStatusDetailComponent, DossierStatusUpdateComponent, DossierStatusDeleteDialogComponent],
})
export class DossierStatusModule {}
