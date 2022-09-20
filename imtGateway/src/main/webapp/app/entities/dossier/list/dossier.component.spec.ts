import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DossierService } from '../service/dossier.service';

import { DossierComponent } from './dossier.component';

describe('Dossier Management Component', () => {
  let comp: DossierComponent;
  let fixture: ComponentFixture<DossierComponent>;
  let service: DossierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'dossier', component: DossierComponent }]), HttpClientTestingModule],
      declarations: [DossierComponent],
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
      .overrideTemplate(DossierComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DossierComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DossierService);

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
    expect(comp.dossiers?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to dossierService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getDossierIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getDossierIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
