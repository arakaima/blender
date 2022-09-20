import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RequestsMessageService } from '../service/requests-message.service';

import { RequestsMessageComponent } from './requests-message.component';

describe('RequestsMessage Management Component', () => {
  let comp: RequestsMessageComponent;
  let fixture: ComponentFixture<RequestsMessageComponent>;
  let service: RequestsMessageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'requests-message', component: RequestsMessageComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [RequestsMessageComponent],
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
      .overrideTemplate(RequestsMessageComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RequestsMessageComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RequestsMessageService);

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
    expect(comp.requestsMessages?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to requestsMessageService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getRequestsMessageIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getRequestsMessageIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
