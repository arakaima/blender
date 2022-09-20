import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExpertDetailComponent } from './expert-detail.component';

describe('Expert Management Detail Component', () => {
  let comp: ExpertDetailComponent;
  let fixture: ComponentFixture<ExpertDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ExpertDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ expert: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(ExpertDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ExpertDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load expert on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.expert).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
