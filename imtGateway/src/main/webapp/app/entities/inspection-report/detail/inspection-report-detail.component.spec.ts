import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InspectionReportDetailComponent } from './inspection-report-detail.component';

describe('InspectionReport Management Detail Component', () => {
  let comp: InspectionReportDetailComponent;
  let fixture: ComponentFixture<InspectionReportDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InspectionReportDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ inspectionReport: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(InspectionReportDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InspectionReportDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load inspectionReport on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.inspectionReport).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
