import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInspectionType } from '../inspection-type.model';

@Component({
  selector: 'jhi-inspection-type-detail',
  templateUrl: './inspection-type-detail.component.html',
})
export class InspectionTypeDetailComponent implements OnInit {
  inspectionType: IInspectionType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspectionType }) => {
      this.inspectionType = inspectionType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
