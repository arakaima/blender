import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ContactPersonService } from '../service/contact-person.service';

import { ContactPersonComponent } from './contact-person.component';

describe('ContactPerson Management Component', () => {
  let comp: ContactPersonComponent;
  let fixture: ComponentFixture<ContactPersonComponent>;
  let service: ContactPersonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'contact-person', component: ContactPersonComponent }]), HttpClientTestingModule],
      declarations: [ContactPersonComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ContactPersonComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContactPersonComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ContactPersonService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.contactPeople?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to contactPersonService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getContactPersonIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getContactPersonIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
