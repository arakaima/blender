import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DossierStatusDetailComponent } from './dossier-status-detail.component';

describe('DossierStatus Management Detail Component', () => {
  let comp: DossierStatusDetailComponent;
  let fixture: ComponentFixture<DossierStatusDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DossierStatusDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dossierStatus: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(DossierStatusDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DossierStatusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dossierStatus on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dossierStatus).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
