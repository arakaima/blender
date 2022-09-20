import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CapaDetailComponent } from './capa-detail.component';

describe('Capa Management Detail Component', () => {
  let comp: CapaDetailComponent;
  let fixture: ComponentFixture<CapaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CapaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ capa: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(CapaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CapaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load capa on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.capa).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
