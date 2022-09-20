import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DossierTypeFormService } from './dossier-type-form.service';
import { DossierTypeService } from '../service/dossier-type.service';
import { IDossierType } from '../dossier-type.model';

import { DossierTypeUpdateComponent } from './dossier-type-update.component';

describe('DossierType Management Update Component', () => {
  let comp: DossierTypeUpdateComponent;
  let fixture: ComponentFixture<DossierTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dossierTypeFormService: DossierTypeFormService;
  let dossierTypeService: DossierTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DossierTypeUpdateComponent],
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
      .overrideTemplate(DossierTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DossierTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dossierTypeFormService = TestBed.inject(DossierTypeFormService);
    dossierTypeService = TestBed.inject(DossierTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const dossierType: IDossierType = { id: 'CBA' };

      activatedRoute.data = of({ dossierType });
      comp.ngOnInit();

      expect(comp.dossierType).toEqual(dossierType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDossierType>>();
      const dossierType = { id: 'ABC' };
      jest.spyOn(dossierTypeFormService, 'getDossierType').mockReturnValue(dossierType);
      jest.spyOn(dossierTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dossierType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dossierType }));
      saveSubject.complete();

      // THEN
      expect(dossierTypeFormService.getDossierType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dossierTypeService.update).toHaveBeenCalledWith(expect.objectContaining(dossierType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDossierType>>();
      const dossierType = { id: 'ABC' };
      jest.spyOn(dossierTypeFormService, 'getDossierType').mockReturnValue({ id: null });
      jest.spyOn(dossierTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dossierType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dossierType }));
      saveSubject.complete();

      // THEN
      expect(dossierTypeFormService.getDossierType).toHaveBeenCalled();
      expect(dossierTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDossierType>>();
      const dossierType = { id: 'ABC' };
      jest.spyOn(dossierTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dossierType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dossierTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
