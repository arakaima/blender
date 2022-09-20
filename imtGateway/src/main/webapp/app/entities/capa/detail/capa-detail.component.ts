import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICapa } from '../capa.model';

@Component({
  selector: 'jhi-capa-detail',
  templateUrl: './capa-detail.component.html',
})
export class CapaDetailComponent implements OnInit {
  capa: ICapa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ capa }) => {
      this.capa = capa;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
