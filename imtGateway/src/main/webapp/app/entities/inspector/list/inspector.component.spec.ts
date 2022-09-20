import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InspectorService } from '../service/inspector.service';

import { InspectorComponent } from './inspector.component';

describe('Inspector Management Component', () => {
  let comp: InspectorComponent;
  let fixture: ComponentFixture<InspectorComponent>;
  let service: InspectorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'inspector', component: InspectorComponent }]), HttpClientTestingModule],
      declarations: [InspectorComponent],
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
      .overrideTemplate(InspectorComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InspectorComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(InspectorService);

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
    expect(comp.inspectors?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to inspectorService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getInspectorIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getInspectorIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
