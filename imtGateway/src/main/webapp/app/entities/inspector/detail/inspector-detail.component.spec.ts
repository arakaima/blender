import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InspectorDetailComponent } from './inspector-detail.component';

describe('Inspector Management Detail Component', () => {
  let comp: InspectorDetailComponent;
  let fixture: ComponentFixture<InspectorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InspectorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ inspector: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(InspectorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InspectorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load inspector on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.inspector).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
