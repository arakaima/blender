import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DossierStatusFormService } from './dossier-status-form.service';
import { DossierStatusService } from '../service/dossier-status.service';
import { IDossierStatus } from '../dossier-status.model';

import { DossierStatusUpdateComponent } from './dossier-status-update.component';

describe('DossierStatus Management Update Component', () => {
  let comp: DossierStatusUpdateComponent;
  let fixture: ComponentFixture<DossierStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dossierStatusFormService: DossierStatusFormService;
  let dossierStatusService: DossierStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DossierStatusUpdateComponent],
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
      .overrideTemplate(DossierStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DossierStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dossierStatusFormService = TestBed.inject(DossierStatusFormService);
    dossierStatusService = TestBed.inject(DossierStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const dossierStatus: IDossierStatus = { id: 'CBA' };

      activatedRoute.data = of({ dossierStatus });
      comp.ngOnInit();

      expect(comp.dossierStatus).toEqual(dossierStatus);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDossierStatus>>();
      const dossierStatus = { id: 'ABC' };
      jest.spyOn(dossierStatusFormService, 'getDossierStatus').mockReturnValue(dossierStatus);
      jest.spyOn(dossierStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dossierStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dossierStatus }));
      saveSubject.complete();

      // THEN
      expect(dossierStatusFormService.getDossierStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dossierStatusService.update).toHaveBeenCalledWith(expect.objectContaining(dossierStatus));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDossierStatus>>();
      const dossierStatus = { id: 'ABC' };
      jest.spyOn(dossierStatusFormService, 'getDossierStatus').mockReturnValue({ id: null });
      jest.spyOn(dossierStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dossierStatus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dossierStatus }));
      saveSubject.complete();

      // THEN
      expect(dossierStatusFormService.getDossierStatus).toHaveBeenCalled();
      expect(dossierStatusService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDossierStatus>>();
      const dossierStatus = { id: 'ABC' };
      jest.spyOn(dossierStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dossierStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dossierStatusService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
