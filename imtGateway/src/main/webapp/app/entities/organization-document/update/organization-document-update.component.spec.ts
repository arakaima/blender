import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrganizationDocumentFormService } from './organization-document-form.service';
import { OrganizationDocumentService } from '../service/organization-document.service';
import { IOrganizationDocument } from '../organization-document.model';

import { OrganizationDocumentUpdateComponent } from './organization-document-update.component';

describe('OrganizationDocument Management Update Component', () => {
  let comp: OrganizationDocumentUpdateComponent;
  let fixture: ComponentFixture<OrganizationDocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let organizationDocumentFormService: OrganizationDocumentFormService;
  let organizationDocumentService: OrganizationDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrganizationDocumentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OrganizationDocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrganizationDocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    organizationDocumentFormService = TestBed.inject(OrganizationDocumentFormService);
    organizationDocumentService = TestBed.inject(OrganizationDocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const organizationDocument: IOrganizationDocument = { id: 'CBA' };

      activatedRoute.data = of({ organizationDocument });
      comp.ngOnInit();

      expect(comp.organizationDocument).toEqual(organizationDocument);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationDocument>>();
      const organizationDocument = { id: 'ABC' };
      jest.spyOn(organizationDocumentFormService, 'getOrganizationDocument').mockReturnValue(organizationDocument);
      jest.spyOn(organizationDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organizationDocument }));
      saveSubject.complete();

      // THEN
      expect(organizationDocumentFormService.getOrganizationDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(organizationDocumentService.update).toHaveBeenCalledWith(expect.objectContaining(organizationDocument));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationDocument>>();
      const organizationDocument = { id: 'ABC' };
      jest.spyOn(organizationDocumentFormService, 'getOrganizationDocument').mockReturnValue({ id: null });
      jest.spyOn(organizationDocumentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationDocument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organizationDocument }));
      saveSubject.complete();

      // THEN
      expect(organizationDocumentFormService.getOrganizationDocument).toHaveBeenCalled();
      expect(organizationDocumentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationDocument>>();
      const organizationDocument = { id: 'ABC' };
      jest.spyOn(organizationDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(organizationDocumentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
