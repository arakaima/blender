import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DeficiencyService } from '../service/deficiency.service';

import { DeficiencyComponent } from './deficiency.component';

describe('Deficiency Management Component', () => {
  let comp: DeficiencyComponent;
  let fixture: ComponentFixture<DeficiencyComponent>;
  let service: DeficiencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'deficiency', component: DeficiencyComponent }]), HttpClientTestingModule],
      declarations: [DeficiencyComponent],
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
      .overrideTemplate(DeficiencyComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeficiencyComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DeficiencyService);

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
    expect(comp.deficiencies?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to deficiencyService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getDeficiencyIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getDeficiencyIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
