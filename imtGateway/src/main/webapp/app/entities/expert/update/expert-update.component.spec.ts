import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ExpertFormService } from './expert-form.service';
import { ExpertService } from '../service/expert.service';
import { IExpert } from '../expert.model';

import { ExpertUpdateComponent } from './expert-update.component';

describe('Expert Management Update Component', () => {
  let comp: ExpertUpdateComponent;
  let fixture: ComponentFixture<ExpertUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let expertFormService: ExpertFormService;
  let expertService: ExpertService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ExpertUpdateComponent],
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
      .overrideTemplate(ExpertUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExpertUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    expertFormService = TestBed.inject(ExpertFormService);
    expertService = TestBed.inject(ExpertService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const expert: IExpert = { id: 'CBA' };

      activatedRoute.data = of({ expert });
      comp.ngOnInit();

      expect(comp.expert).toEqual(expert);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpert>>();
      const expert = { id: 'ABC' };
      jest.spyOn(expertFormService, 'getExpert').mockReturnValue(expert);
      jest.spyOn(expertService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expert });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expert }));
      saveSubject.complete();

      // THEN
      expect(expertFormService.getExpert).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(expertService.update).toHaveBeenCalledWith(expect.objectContaining(expert));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpert>>();
      const expert = { id: 'ABC' };
      jest.spyOn(expertFormService, 'getExpert').mockReturnValue({ id: null });
      jest.spyOn(expertService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expert: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expert }));
      saveSubject.complete();

      // THEN
      expect(expertFormService.getExpert).toHaveBeenCalled();
      expect(expertService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpert>>();
      const expert = { id: 'ABC' };
      jest.spyOn(expertService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expert });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(expertService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
