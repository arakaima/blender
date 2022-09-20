import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExpert } from '../expert.model';

@Component({
  selector: 'jhi-expert-detail',
  templateUrl: './expert-detail.component.html',
})
export class ExpertDetailComponent implements OnInit {
  expert: IExpert | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expert }) => {
      this.expert = expert;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
