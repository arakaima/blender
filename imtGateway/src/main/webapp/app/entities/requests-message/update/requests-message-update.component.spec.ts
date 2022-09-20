import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RequestsMessageFormService } from './requests-message-form.service';
import { RequestsMessageService } from '../service/requests-message.service';
import { IRequestsMessage } from '../requests-message.model';

import { RequestsMessageUpdateComponent } from './requests-message-update.component';

describe('RequestsMessage Management Update Component', () => {
  let comp: RequestsMessageUpdateComponent;
  let fixture: ComponentFixture<RequestsMessageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let requestsMessageFormService: RequestsMessageFormService;
  let requestsMessageService: RequestsMessageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RequestsMessageUpdateComponent],
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
      .overrideTemplate(RequestsMessageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RequestsMessageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    requestsMessageFormService = TestBed.inject(RequestsMessageFormService);
    requestsMessageService = TestBed.inject(RequestsMessageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const requestsMessage: IRequestsMessage = { id: 'CBA' };

      activatedRoute.data = of({ requestsMessage });
      comp.ngOnInit();

      expect(comp.requestsMessage).toEqual(requestsMessage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequestsMessage>>();
      const requestsMessage = { id: 'ABC' };
      jest.spyOn(requestsMessageFormService, 'getRequestsMessage').mockReturnValue(requestsMessage);
      jest.spyOn(requestsMessageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ requestsMessage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: requestsMessage }));
      saveSubject.complete();

      // THEN
      expect(requestsMessageFormService.getRequestsMessage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(requestsMessageService.update).toHaveBeenCalledWith(expect.objectContaining(requestsMessage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequestsMessage>>();
      const requestsMessage = { id: 'ABC' };
      jest.spyOn(requestsMessageFormService, 'getRequestsMessage').mockReturnValue({ id: null });
      jest.spyOn(requestsMessageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ requestsMessage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: requestsMessage }));
      saveSubject.complete();

      // THEN
      expect(requestsMessageFormService.getRequestsMessage).toHaveBeenCalled();
      expect(requestsMessageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequestsMessage>>();
      const requestsMessage = { id: 'ABC' };
      jest.spyOn(requestsMessageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ requestsMessage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(requestsMessageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
