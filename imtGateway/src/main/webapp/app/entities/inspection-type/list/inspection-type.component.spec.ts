import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InspectionTypeService } from '../service/inspection-type.service';

import { InspectionTypeComponent } from './inspection-type.component';

describe('InspectionType Management Component', () => {
  let comp: InspectionTypeComponent;
  let fixture: ComponentFixture<InspectionTypeComponent>;
  let service: InspectionTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'inspection-type', component: InspectionTypeComponent }]), HttpClientTestingModule],
      declarations: [InspectionTypeComponent],
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
      .overrideTemplate(InspectionTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InspectionTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(InspectionTypeService);

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
    expect(comp.inspectionTypes?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to inspectionTypeService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getInspectionTypeIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getInspectionTypeIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
