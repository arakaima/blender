import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InspectorDossierService } from '../service/inspector-dossier.service';

import { InspectorDossierComponent } from './inspector-dossier.component';

describe('InspectorDossier Management Component', () => {
  let comp: InspectorDossierComponent;
  let fixture: ComponentFixture<InspectorDossierComponent>;
  let service: InspectorDossierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'inspector-dossier', component: InspectorDossierComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [InspectorDossierComponent],
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
      .overrideTemplate(InspectorDossierComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InspectorDossierComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(InspectorDossierService);

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
    expect(comp.inspectorDossiers?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to inspectorDossierService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getInspectorDossierIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getInspectorDossierIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
