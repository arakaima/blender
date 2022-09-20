import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CapaFormService } from './capa-form.service';
import { CapaService } from '../service/capa.service';
import { ICapa } from '../capa.model';

import { CapaUpdateComponent } from './capa-update.component';

describe('Capa Management Update Component', () => {
  let comp: CapaUpdateComponent;
  let fixture: ComponentFixture<CapaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let capaFormService: CapaFormService;
  let capaService: CapaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CapaUpdateComponent],
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
      .overrideTemplate(CapaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CapaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    capaFormService = TestBed.inject(CapaFormService);
    capaService = TestBed.inject(CapaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const capa: ICapa = { id: 'CBA' };

      activatedRoute.data = of({ capa });
      comp.ngOnInit();

      expect(comp.capa).toEqual(capa);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICapa>>();
      const capa = { id: 'ABC' };
      jest.spyOn(capaFormService, 'getCapa').mockReturnValue(capa);
      jest.spyOn(capaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ capa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: capa }));
      saveSubject.complete();

      // THEN
      expect(capaFormService.getCapa).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(capaService.update).toHaveBeenCalledWith(expect.objectContaining(capa));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICapa>>();
      const capa = { id: 'ABC' };
      jest.spyOn(capaFormService, 'getCapa').mockReturnValue({ id: null });
      jest.spyOn(capaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ capa: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: capa }));
      saveSubject.complete();

      // THEN
      expect(capaFormService.getCapa).toHaveBeenCalled();
      expect(capaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICapa>>();
      const capa = { id: 'ABC' };
      jest.spyOn(capaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ capa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(capaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
