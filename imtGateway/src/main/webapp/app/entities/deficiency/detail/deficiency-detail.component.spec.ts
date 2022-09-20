import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeficiencyDetailComponent } from './deficiency-detail.component';

describe('Deficiency Management Detail Component', () => {
  let comp: DeficiencyDetailComponent;
  let fixture: ComponentFixture<DeficiencyDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeficiencyDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deficiency: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(DeficiencyDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeficiencyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deficiency on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deficiency).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
