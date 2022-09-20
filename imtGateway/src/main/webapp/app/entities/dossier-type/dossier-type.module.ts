import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DossierTypeComponent } from './list/dossier-type.component';
import { DossierTypeDetailComponent } from './detail/dossier-type-detail.component';
import { DossierTypeUpdateComponent } from './update/dossier-type-update.component';
import { DossierTypeDeleteDialogComponent } from './delete/dossier-type-delete-dialog.component';
import { DossierTypeRoutingModule } from './route/dossier-type-routing.module';

@NgModule({
  imports: [SharedModule, DossierTypeRoutingModule],
  declarations: [DossierTypeComponent, DossierTypeDetailComponent, DossierTypeUpdateComponent, DossierTypeDeleteDialogComponent],
})
export class DossierTypeModule {}
