import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInspectorDossier } from '../inspector-dossier.model';

@Component({
  selector: 'jhi-inspector-dossier-detail',
  templateUrl: './inspector-dossier-detail.component.html',
})
export class InspectorDossierDetailComponent implements OnInit {
  inspectorDossier: IInspectorDossier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspectorDossier }) => {
      this.inspectorDossier = inspectorDossier;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
