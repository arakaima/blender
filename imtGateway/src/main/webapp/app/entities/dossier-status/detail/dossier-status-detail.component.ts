import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDossierStatus } from '../dossier-status.model';

@Component({
  selector: 'jhi-dossier-status-detail',
  templateUrl: './dossier-status-detail.component.html',
})
export class DossierStatusDetailComponent implements OnInit {
  dossierStatus: IDossierStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dossierStatus }) => {
      this.dossierStatus = dossierStatus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
