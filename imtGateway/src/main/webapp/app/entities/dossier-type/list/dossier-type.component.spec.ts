import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DossierTypeService } from '../service/dossier-type.service';

import { DossierTypeComponent } from './dossier-type.component';

describe('DossierType Management Component', () => {
  let comp: DossierTypeComponent;
  let fixture: ComponentFixture<DossierTypeComponent>;
  let service: DossierTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'dossier-type', component: DossierTypeComponent }]), HttpClientTestingModule],
      declarations: [DossierTypeComponent],
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
      .overrideTemplate(DossierTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DossierTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DossierTypeService);

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
    expect(comp.dossierTypes?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to dossierTypeService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getDossierTypeIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getDossierTypeIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
