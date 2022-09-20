import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InspectorFormService } from './inspector-form.service';
import { InspectorService } from '../service/inspector.service';
import { IInspector } from '../inspector.model';

import { InspectorUpdateComponent } from './inspector-update.component';

describe('Inspector Management Update Component', () => {
  let comp: InspectorUpdateComponent;
  let fixture: ComponentFixture<InspectorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inspectorFormService: InspectorFormService;
  let inspectorService: InspectorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InspectorUpdateComponent],
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
      .overrideTemplate(InspectorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InspectorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inspectorFormService = TestBed.inject(InspectorFormService);
    inspectorService = TestBed.inject(InspectorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const inspector: IInspector = { id: 'CBA' };

      activatedRoute.data = of({ inspector });
      comp.ngOnInit();

      expect(comp.inspector).toEqual(inspector);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspector>>();
      const inspector = { id: 'ABC' };
      jest.spyOn(inspectorFormService, 'getInspector').mockReturnValue(inspector);
      jest.spyOn(inspectorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspector });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspector }));
      saveSubject.complete();

      // THEN
      expect(inspectorFormService.getInspector).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inspectorService.update).toHaveBeenCalledWith(expect.objectContaining(inspector));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspector>>();
      const inspector = { id: 'ABC' };
      jest.spyOn(inspectorFormService, 'getInspector').mockReturnValue({ id: null });
      jest.spyOn(inspectorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspector: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspector }));
      saveSubject.complete();

      // THEN
      expect(inspectorFormService.getInspector).toHaveBeenCalled();
      expect(inspectorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInspector>>();
      const inspector = { id: 'ABC' };
      jest.spyOn(inspectorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspector });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inspectorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
