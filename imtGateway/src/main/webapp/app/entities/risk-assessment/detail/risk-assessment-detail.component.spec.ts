import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RiskAssessmentDetailComponent } from './risk-assessment-detail.component';

describe('RiskAssessment Management Detail Component', () => {
  let comp: RiskAssessmentDetailComponent;
  let fixture: ComponentFixture<RiskAssessmentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RiskAssessmentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ riskAssessment: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(RiskAssessmentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RiskAssessmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load riskAssessment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.riskAssessment).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
