import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContactPerson } from '../contact-person.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../contact-person.test-samples';

import { ContactPersonService } from './contact-person.service';

const requireRestSample: IContactPerson = {
  ...sampleWithRequiredData,
};

describe('ContactPerson Service', () => {
  let service: ContactPersonService;
  let httpMock: HttpTestingController;
  let expectedResult: IContactPerson | IContactPerson[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContactPersonService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ContactPerson', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const contactPerson = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contactPerson).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContactPerson', () => {
      const contactPerson = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contactPerson).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContactPerson', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContactPerson', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContactPerson', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addContactPersonToCollectionIfMissing', () => {
      it('should add a ContactPerson to an empty array', () => {
        const contactPerson: IContactPerson = sampleWithRequiredData;
        expectedResult = service.addContactPersonToCollectionIfMissing([], contactPerson);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contactPerson);
      });

      it('should not add a ContactPerson to an array that contains it', () => {
        const contactPerson: IContactPerson = sampleWithRequiredData;
        const contactPersonCollection: IContactPerson[] = [
          {
            ...contactPerson,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContactPersonToCollectionIfMissing(contactPersonCollection, contactPerson);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContactPerson to an array that doesn't contain it", () => {
        const contactPerson: IContactPerson = sampleWithRequiredData;
        const contactPersonCollection: IContactPerson[] = [sampleWithPartialData];
        expectedResult = service.addContactPersonToCollectionIfMissing(contactPersonCollection, contactPerson);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contactPerson);
      });

      it('should add only unique ContactPerson to an array', () => {
        const contactPersonArray: IContactPerson[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contactPersonCollection: IContactPerson[] = [sampleWithRequiredData];
        expectedResult = service.addContactPersonToCollectionIfMissing(contactPersonCollection, ...contactPersonArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contactPerson: IContactPerson = sampleWithRequiredData;
        const contactPerson2: IContactPerson = sampleWithPartialData;
        expectedResult = service.addContactPersonToCollectionIfMissing([], contactPerson, contactPerson2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contactPerson);
        expect(expectedResult).toContain(contactPerson2);
      });

      it('should accept null and undefined values', () => {
        const contactPerson: IContactPerson = sampleWithRequiredData;
        expectedResult = service.addContactPersonToCollectionIfMissing([], null, contactPerson, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contactPerson);
      });

      it('should return initial array if no ContactPerson is added', () => {
        const contactPersonCollection: IContactPerson[] = [sampleWithRequiredData];
        expectedResult = service.addContactPersonToCollectionIfMissing(contactPersonCollection, undefined, null);
        expect(expectedResult).toEqual(contactPersonCollection);
      });
    });

    describe('compareContactPerson', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContactPerson(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareContactPerson(entity1, entity2);
        const compareResult2 = service.compareContactPerson(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareContactPerson(entity1, entity2);
        const compareResult2 = service.compareContactPerson(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareContactPerson(entity1, entity2);
        const compareResult2 = service.compareContactPerson(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
