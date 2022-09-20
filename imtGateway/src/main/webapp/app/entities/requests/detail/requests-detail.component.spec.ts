import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RequestsDetailComponent } from './requests-detail.component';

describe('Requests Management Detail Component', () => {
  let comp: RequestsDetailComponent;
  let fixture: ComponentFixture<RequestsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RequestsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ requests: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(RequestsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RequestsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load requests on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.requests).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
