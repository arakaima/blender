import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRiskAssessment } from '../risk-assessment.model';

@Component({
  selector: 'jhi-risk-assessment-detail',
  templateUrl: './risk-assessment-detail.component.html',
})
export class RiskAssessmentDetailComponent implements OnInit {
  riskAssessment: IRiskAssessment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ riskAssessment }) => {
      this.riskAssessment = riskAssessment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
