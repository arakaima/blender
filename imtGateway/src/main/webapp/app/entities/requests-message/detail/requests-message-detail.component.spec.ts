import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RequestsMessageDetailComponent } from './requests-message-detail.component';

describe('RequestsMessage Management Detail Component', () => {
  let comp: RequestsMessageDetailComponent;
  let fixture: ComponentFixture<RequestsMessageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RequestsMessageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ requestsMessage: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(RequestsMessageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RequestsMessageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load requestsMessage on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.requestsMessage).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
