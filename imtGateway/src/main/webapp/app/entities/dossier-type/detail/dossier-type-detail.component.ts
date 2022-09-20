import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDossierType } from '../dossier-type.model';

@Component({
  selector: 'jhi-dossier-type-detail',
  templateUrl: './dossier-type-detail.component.html',
})
export class DossierTypeDetailComponent implements OnInit {
  dossierType: IDossierType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dossierType }) => {
      this.dossierType = dossierType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
