import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RequestsService } from '../service/requests.service';

import { RequestsComponent } from './requests.component';

describe('Requests Management Component', () => {
  let comp: RequestsComponent;
  let fixture: ComponentFixture<RequestsComponent>;
  let service: RequestsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'requests', component: RequestsComponent }]), HttpClientTestingModule],
      declarations: [RequestsComponent],
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
      .overrideTemplate(RequestsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RequestsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RequestsService);

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
    expect(comp.requests?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to requestsService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getRequestsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getRequestsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
