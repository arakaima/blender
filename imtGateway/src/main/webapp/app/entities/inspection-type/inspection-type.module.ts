import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InspectionTypeComponent } from './list/inspection-type.component';
import { InspectionTypeDetailComponent } from './detail/inspection-type-detail.component';
import { InspectionTypeUpdateComponent } from './update/inspection-type-update.component';
import { InspectionTypeDeleteDialogComponent } from './delete/inspection-type-delete-dialog.component';
import { InspectionTypeRoutingModule } from './route/inspection-type-routing.module';

@NgModule({
  imports: [SharedModule, InspectionTypeRoutingModule],
  declarations: [
    InspectionTypeComponent,
    InspectionTypeDetailComponent,
    InspectionTypeUpdateComponent,
    InspectionTypeDeleteDialogComponent,
  ],
})
export class InspectionTypeModule {}
