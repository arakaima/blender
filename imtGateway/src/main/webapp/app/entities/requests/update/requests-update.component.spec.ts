import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RequestsFormService } from './requests-form.service';
import { RequestsService } from '../service/requests.service';
import { IRequests } from '../requests.model';

import { RequestsUpdateComponent } from './requests-update.component';

describe('Requests Management Update Component', () => {
  let comp: RequestsUpdateComponent;
  let fixture: ComponentFixture<RequestsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let requestsFormService: RequestsFormService;
  let requestsService: RequestsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RequestsUpdateComponent],
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
      .overrideTemplate(RequestsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RequestsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    requestsFormService = TestBed.inject(RequestsFormService);
    requestsService = TestBed.inject(RequestsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const requests: IRequests = { id: 'CBA' };

      activatedRoute.data = of({ requests });
      comp.ngOnInit();

      expect(comp.requests).toEqual(requests);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequests>>();
      const requests = { id: 'ABC' };
      jest.spyOn(requestsFormService, 'getRequests').mockReturnValue(requests);
      jest.spyOn(requestsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ requests });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: requests }));
      saveSubject.complete();

      // THEN
      expect(requestsFormService.getRequests).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(requestsService.update).toHaveBeenCalledWith(expect.objectContaining(requests));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequests>>();
      const requests = { id: 'ABC' };
      jest.spyOn(requestsFormService, 'getRequests').mockReturnValue({ id: null });
      jest.spyOn(requestsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ requests: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: requests }));
      saveSubject.complete();

      // THEN
      expect(requestsFormService.getRequests).toHaveBeenCalled();
      expect(requestsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequests>>();
      const requests = { id: 'ABC' };
      jest.spyOn(requestsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ requests });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(requestsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
