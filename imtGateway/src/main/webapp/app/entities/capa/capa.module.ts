import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CapaComponent } from './list/capa.component';
import { CapaDetailComponent } from './detail/capa-detail.component';
import { CapaUpdateComponent } from './update/capa-update.component';
import { CapaDeleteDialogComponent } from './delete/capa-delete-dialog.component';
import { CapaRoutingModule } from './route/capa-routing.module';

@NgModule({
  imports: [SharedModule, CapaRoutingModule],
  declarations: [CapaComponent, CapaDetailComponent, CapaUpdateComponent, CapaDeleteDialogComponent],
})
export class CapaModule {}
