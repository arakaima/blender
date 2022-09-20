import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInspectionReport } from '../inspection-report.model';

@Component({
  selector: 'jhi-inspection-report-detail',
  templateUrl: './inspection-report-detail.component.html',
})
export class InspectionReportDetailComponent implements OnInit {
  inspectionReport: IInspectionReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspectionReport }) => {
      this.inspectionReport = inspectionReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
