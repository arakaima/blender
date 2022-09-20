import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactPerson } from '../contact-person.model';

@Component({
  selector: 'jhi-contact-person-detail',
  templateUrl: './contact-person-detail.component.html',
})
export class ContactPersonDetailComponent implements OnInit {
  contactPerson: IContactPerson | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactPerson }) => {
      this.contactPerson = contactPerson;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
