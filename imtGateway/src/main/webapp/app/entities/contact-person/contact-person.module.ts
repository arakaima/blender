import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactPersonComponent } from './list/contact-person.component';
import { ContactPersonDetailComponent } from './detail/contact-person-detail.component';
import { ContactPersonUpdateComponent } from './update/contact-person-update.component';
import { ContactPersonDeleteDialogComponent } from './delete/contact-person-delete-dialog.component';
import { ContactPersonRoutingModule } from './route/contact-person-routing.module';

@NgModule({
  imports: [SharedModule, ContactPersonRoutingModule],
  declarations: [ContactPersonComponent, ContactPersonDetailComponent, ContactPersonUpdateComponent, ContactPersonDeleteDialogComponent],
})
export class ContactPersonModule {}
