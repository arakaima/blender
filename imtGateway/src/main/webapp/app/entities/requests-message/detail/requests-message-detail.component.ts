import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRequestsMessage } from '../requests-message.model';

@Component({
  selector: 'jhi-requests-message-detail',
  templateUrl: './requests-message-detail.component.html',
})
export class RequestsMessageDetailComponent implements OnInit {
  requestsMessage: IRequestsMessage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ requestsMessage }) => {
      this.requestsMessage = requestsMessage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
