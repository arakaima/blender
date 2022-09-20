import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InspectorDossierDetailComponent } from './inspector-dossier-detail.component';

describe('InspectorDossier Management Detail Component', () => {
  let comp: InspectorDossierDetailComponent;
  let fixture: ComponentFixture<InspectorDossierDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InspectorDossierDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ inspectorDossier: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(InspectorDossierDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InspectorDossierDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load inspectorDossier on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.inspectorDossier).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
