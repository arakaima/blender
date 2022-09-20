import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AttachedDocumentService } from '../service/attached-document.service';

import { AttachedDocumentComponent } from './attached-document.component';

describe('AttachedDocument Management Component', () => {
  let comp: AttachedDocumentComponent;
  let fixture: ComponentFixture<AttachedDocumentComponent>;
  let service: AttachedDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'attached-document', component: AttachedDocumentComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [AttachedDocumentComponent],
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
      .overrideTemplate(AttachedDocumentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttachedDocumentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AttachedDocumentService);

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
    expect(comp.attachedDocuments?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to attachedDocumentService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getAttachedDocumentIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAttachedDocumentIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
