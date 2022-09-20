import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrganizationDocumentService } from '../service/organization-document.service';

import { OrganizationDocumentComponent } from './organization-document.component';

describe('OrganizationDocument Management Component', () => {
  let comp: OrganizationDocumentComponent;
  let fixture: ComponentFixture<OrganizationDocumentComponent>;
  let service: OrganizationDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'organization-document', component: OrganizationDocumentComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [OrganizationDocumentComponent],
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
      .overrideTemplate(OrganizationDocumentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrganizationDocumentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OrganizationDocumentService);

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
    expect(comp.organizationDocuments?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to organizationDocumentService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getOrganizationDocumentIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getOrganizationDocumentIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
