import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeficiencyFormService } from './deficiency-form.service';
import { DeficiencyService } from '../service/deficiency.service';
import { IDeficiency } from '../deficiency.model';

import { DeficiencyUpdateComponent } from './deficiency-update.component';

describe('Deficiency Management Update Component', () => {
  let comp: DeficiencyUpdateComponent;
  let fixture: ComponentFixture<DeficiencyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deficiencyFormService: DeficiencyFormService;
  let deficiencyService: DeficiencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeficiencyUpdateComponent],
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
      .overrideTemplate(DeficiencyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeficiencyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deficiencyFormService = TestBed.inject(DeficiencyFormService);
    deficiencyService = TestBed.inject(DeficiencyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const deficiency: IDeficiency = { id: 'CBA' };

      activatedRoute.data = of({ deficiency });
      comp.ngOnInit();

      expect(comp.deficiency).toEqual(deficiency);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeficiency>>();
      const deficiency = { id: 'ABC' };
      jest.spyOn(deficiencyFormService, 'getDeficiency').mockReturnValue(deficiency);
      jest.spyOn(deficiencyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deficiency });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deficiency }));
      saveSubject.complete();

      // THEN
      expect(deficiencyFormService.getDeficiency).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(deficiencyService.update).toHaveBeenCalledWith(expect.objectContaining(deficiency));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeficiency>>();
      const deficiency = { id: 'ABC' };
      jest.spyOn(deficiencyFormService, 'getDeficiency').mockReturnValue({ id: null });
      jest.spyOn(deficiencyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deficiency: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deficiency }));
      saveSubject.complete();

      // THEN
      expect(deficiencyFormService.getDeficiency).toHaveBeenCalled();
      expect(deficiencyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeficiency>>();
      const deficiency = { id: 'ABC' };
      jest.spyOn(deficiencyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deficiency });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deficiencyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
