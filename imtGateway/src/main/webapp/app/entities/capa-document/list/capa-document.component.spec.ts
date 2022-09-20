import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CapaDocumentService } from '../service/capa-document.service';

import { CapaDocumentComponent } from './capa-document.component';

describe('CapaDocument Management Component', () => {
  let comp: CapaDocumentComponent;
  let fixture: ComponentFixture<CapaDocumentComponent>;
  let service: CapaDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'capa-document', component: CapaDocumentComponent }]), HttpClientTestingModule],
      declarations: [CapaDocumentComponent],
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
      .overrideTemplate(CapaDocumentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CapaDocumentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CapaDocumentService);

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
    expect(comp.capaDocuments?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to capaDocumentService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getCapaDocumentIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCapaDocumentIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
