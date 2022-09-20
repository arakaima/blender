import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ExpertService } from '../service/expert.service';

import { ExpertComponent } from './expert.component';

describe('Expert Management Component', () => {
  let comp: ExpertComponent;
  let fixture: ComponentFixture<ExpertComponent>;
  let service: ExpertService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'expert', component: ExpertComponent }]), HttpClientTestingModule],
      declarations: [ExpertComponent],
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
      .overrideTemplate(ExpertComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExpertComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ExpertService);

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
    expect(comp.experts?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to expertService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getExpertIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getExpertIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
