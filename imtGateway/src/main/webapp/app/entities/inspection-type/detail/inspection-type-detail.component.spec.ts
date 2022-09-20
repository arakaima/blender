import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InspectionTypeDetailComponent } from './inspection-type-detail.component';

describe('InspectionType Management Detail Component', () => {
  let comp: InspectionTypeDetailComponent;
  let fixture: ComponentFixture<InspectionTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InspectionTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ inspectionType: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(InspectionTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InspectionTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load inspectionType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.inspectionType).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
