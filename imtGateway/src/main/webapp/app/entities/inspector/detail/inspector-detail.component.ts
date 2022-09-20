import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInspector } from '../inspector.model';

@Component({
  selector: 'jhi-inspector-detail',
  templateUrl: './inspector-detail.component.html',
})
export class InspectorDetailComponent implements OnInit {
  inspector: IInspector | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspector }) => {
      this.inspector = inspector;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
