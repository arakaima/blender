import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContactPersonFormService } from './contact-person-form.service';
import { ContactPersonService } from '../service/contact-person.service';
import { IContactPerson } from '../contact-person.model';

import { ContactPersonUpdateComponent } from './contact-person-update.component';

describe('ContactPerson Management Update Component', () => {
  let comp: ContactPersonUpdateComponent;
  let fixture: ComponentFixture<ContactPersonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contactPersonFormService: ContactPersonFormService;
  let contactPersonService: ContactPersonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContactPersonUpdateComponent],
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
      .overrideTemplate(ContactPersonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContactPersonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contactPersonFormService = TestBed.inject(ContactPersonFormService);
    contactPersonService = TestBed.inject(ContactPersonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const contactPerson: IContactPerson = { id: 'CBA' };

      activatedRoute.data = of({ contactPerson });
      comp.ngOnInit();

      expect(comp.contactPerson).toEqual(contactPerson);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContactPerson>>();
      const contactPerson = { id: 'ABC' };
      jest.spyOn(contactPersonFormService, 'getContactPerson').mockReturnValue(contactPerson);
      jest.spyOn(contactPersonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactPerson });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contactPerson }));
      saveSubject.complete();

      // THEN
      expect(contactPersonFormService.getContactPerson).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contactPersonService.update).toHaveBeenCalledWith(expect.objectContaining(contactPerson));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContactPerson>>();
      const contactPerson = { id: 'ABC' };
      jest.spyOn(contactPersonFormService, 'getContactPerson').mockReturnValue({ id: null });
      jest.spyOn(contactPersonService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactPerson: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contactPerson }));
      saveSubject.complete();

      // THEN
      expect(contactPersonFormService.getContactPerson).toHaveBeenCalled();
      expect(contactPersonService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContactPerson>>();
      const contactPerson = { id: 'ABC' };
      jest.spyOn(contactPersonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactPerson });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contactPersonService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
