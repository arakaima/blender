import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DossierStatusService } from '../service/dossier-status.service';

import { DossierStatusComponent } from './dossier-status.component';

describe('DossierStatus Management Component', () => {
  let comp: DossierStatusComponent;
  let fixture: ComponentFixture<DossierStatusComponent>;
  let service: DossierStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'dossier-status', component: DossierStatusComponent }]), HttpClientTestingModule],
      declarations: [DossierStatusComponent],
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
      .overrideTemplate(DossierStatusComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DossierStatusComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DossierStatusService);

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
    expect(comp.dossierStatuses?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to dossierStatusService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getDossierStatusIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getDossierStatusIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
