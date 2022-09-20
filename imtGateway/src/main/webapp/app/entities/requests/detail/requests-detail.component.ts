import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRequests } from '../requests.model';

@Component({
  selector: 'jhi-requests-detail',
  templateUrl: './requests-detail.component.html',
})
export class RequestsDetailComponent implements OnInit {
  requests: IRequests | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ requests }) => {
      this.requests = requests;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
