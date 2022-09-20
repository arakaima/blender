import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InspectorComponent } from './list/inspector.component';
import { InspectorDetailComponent } from './detail/inspector-detail.component';
import { InspectorUpdateComponent } from './update/inspector-update.component';
import { InspectorDeleteDialogComponent } from './delete/inspector-delete-dialog.component';
import { InspectorRoutingModule } from './route/inspector-routing.module';

@NgModule({
  imports: [SharedModule, InspectorRoutingModule],
  declarations: [InspectorComponent, InspectorDetailComponent, InspectorUpdateComponent, InspectorDeleteDialogComponent],
})
export class InspectorModule {}
