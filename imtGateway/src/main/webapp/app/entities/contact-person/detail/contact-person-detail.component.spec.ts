import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactPersonDetailComponent } from './contact-person-detail.component';

describe('ContactPerson Management Detail Component', () => {
  let comp: ContactPersonDetailComponent;
  let fixture: ComponentFixture<ContactPersonDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContactPersonDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ contactPerson: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(ContactPersonDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContactPersonDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load contactPerson on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.contactPerson).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
