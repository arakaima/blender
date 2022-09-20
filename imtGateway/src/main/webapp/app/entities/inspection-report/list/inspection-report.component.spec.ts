import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InspectionReportService } from '../service/inspection-report.service';

import { InspectionReportComponent } from './inspection-report.component';

describe('InspectionReport Management Component', () => {
  let comp: InspectionReportComponent;
  let fixture: ComponentFixture<InspectionReportComponent>;
  let service: InspectionReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'inspection-report', component: InspectionReportComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [InspectionReportComponent],
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
      .overrideTemplate(InspectionReportComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InspectionReportComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(InspectionReportService);

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
    expect(comp.inspectionReports?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to inspectionReportService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getInspectionReportIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getInspectionReportIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
