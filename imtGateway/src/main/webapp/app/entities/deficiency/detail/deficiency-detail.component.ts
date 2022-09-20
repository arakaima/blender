import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeficiency } from '../deficiency.model';

@Component({
  selector: 'jhi-deficiency-detail',
  templateUrl: './deficiency-detail.component.html',
})
export class DeficiencyDetailComponent implements OnInit {
  deficiency: IDeficiency | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deficiency }) => {
      this.deficiency = deficiency;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
