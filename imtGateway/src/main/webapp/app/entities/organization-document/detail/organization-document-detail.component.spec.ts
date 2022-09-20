import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrganizationDocumentDetailComponent } from './organization-document-detail.component';

describe('OrganizationDocument Management Detail Component', () => {
  let comp: OrganizationDocumentDetailComponent;
  let fixture: ComponentFixture<OrganizationDocumentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrganizationDocumentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ organizationDocument: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(OrganizationDocumentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrganizationDocumentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load organizationDocument on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.organizationDocument).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
