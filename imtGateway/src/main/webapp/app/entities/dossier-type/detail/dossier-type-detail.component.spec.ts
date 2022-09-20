import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DossierTypeDetailComponent } from './dossier-type-detail.component';

describe('DossierType Management Detail Component', () => {
  let comp: DossierTypeDetailComponent;
  let fixture: ComponentFixture<DossierTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DossierTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dossierType: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(DossierTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DossierTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dossierType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dossierType).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
