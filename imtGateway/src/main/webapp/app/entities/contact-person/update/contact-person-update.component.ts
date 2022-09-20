import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ContactPersonFormService, ContactPersonFormGroup } from './contact-person-form.service';
import { IContactPerson } from '../contact-person.model';
import { ContactPersonService } from '../service/contact-person.service';

@Component({
  selector: 'jhi-contact-person-update',
  templateUrl: './contact-person-update.component.html',
})
export class ContactPersonUpdateComponent implements OnInit {
  isSaving = false;
  contactPerson: IContactPerson | null = null;

  editForm: ContactPersonFormGroup = this.contactPersonFormService.createContactPersonFormGroup();

  constructor(
    protected contactPersonService: ContactPersonService,
    protected contactPersonFormService: ContactPersonFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactPerson }) => {
      this.contactPerson = contactPerson;
      if (contactPerson) {
        this.updateForm(contactPerson);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contactPerson = this.contactPersonFormService.getContactPerson(this.editForm);
    if (contactPerson.id !== null) {
      this.subscribeToSaveResponse(this.contactPersonService.update(contactPerson));
    } else {
      this.subscribeToSaveResponse(this.contactPersonService.create(contactPerson));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactPerson>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contactPerson: IContactPerson): void {
    this.contactPerson = contactPerson;
    this.contactPersonFormService.resetForm(this.editForm, contactPerson);
  }
}
