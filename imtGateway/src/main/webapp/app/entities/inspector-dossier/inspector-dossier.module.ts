import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InspectorDossierComponent } from './list/inspector-dossier.component';
import { InspectorDossierDetailComponent } from './detail/inspector-dossier-detail.component';
import { InspectorDossierUpdateComponent } from './update/inspector-dossier-update.component';
import { InspectorDossierDeleteDialogComponent } from './delete/inspector-dossier-delete-dialog.component';
import { InspectorDossierRoutingModule } from './route/inspector-dossier-routing.module';

@NgModule({
  imports: [SharedModule, InspectorDossierRoutingModule],
  declarations: [
    InspectorDossierComponent,
    InspectorDossierDetailComponent,
    InspectorDossierUpdateComponent,
    InspectorDossierDeleteDialogComponent,
  ],
})
export class InspectorDossierModule {}
