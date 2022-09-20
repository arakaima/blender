import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RiskAssessmentService } from '../service/risk-assessment.service';

import { RiskAssessmentComponent } from './risk-assessment.component';

describe('RiskAssessment Management Component', () => {
  let comp: RiskAssessmentComponent;
  let fixture: ComponentFixture<RiskAssessmentComponent>;
  let service: RiskAssessmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'risk-assessment', component: RiskAssessmentComponent }]), HttpClientTestingModule],
      declarations: [RiskAssessmentComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(RiskAssessmentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RiskAssessmentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RiskAssessmentService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.riskAssessments?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to riskAssessmentService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getRiskAssessmentIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getRiskAssessmentIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
