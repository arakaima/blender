import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRequests } from '../requests.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../requests.test-samples';

import { RequestsService } from './requests.service';

const requireRestSample: IRequests = {
  ...sampleWithRequiredData,
};

describe('Requests Service', () => {
  let service: RequestsService;
  let httpMock: HttpTestingController;
  let expectedResult: IRequests | IRequests[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RequestsService);
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

    it('should create a Requests', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const requests = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(requests).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Requests', () => {
      const requests = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(requests).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Requests', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Requests', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Requests', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRequestsToCollectionIfMissing', () => {
      it('should add a Requests to an empty array', () => {
        const requests: IRequests = sampleWithRequiredData;
        expectedResult = service.addRequestsToCollectionIfMissing([], requests);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(requests);
      });

      it('should not add a Requests to an array that contains it', () => {
        const requests: IRequests = sampleWithRequiredData;
        const requestsCollection: IRequests[] = [
          {
            ...requests,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRequestsToCollectionIfMissing(requestsCollection, requests);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Requests to an array that doesn't contain it", () => {
        const requests: IRequests = sampleWithRequiredData;
        const requestsCollection: IRequests[] = [sampleWithPartialData];
        expectedResult = service.addRequestsToCollectionIfMissing(requestsCollection, requests);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(requests);
      });

      it('should add only unique Requests to an array', () => {
        const requestsArray: IRequests[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const requestsCollection: IRequests[] = [sampleWithRequiredData];
        expectedResult = service.addRequestsToCollectionIfMissing(requestsCollection, ...requestsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const requests: IRequests = sampleWithRequiredData;
        const requests2: IRequests = sampleWithPartialData;
        expectedResult = service.addRequestsToCollectionIfMissing([], requests, requests2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(requests);
        expect(expectedResult).toContain(requests2);
      });

      it('should accept null and undefined values', () => {
        const requests: IRequests = sampleWithRequiredData;
        expectedResult = service.addRequestsToCollectionIfMissing([], null, requests, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(requests);
      });

      it('should return initial array if no Requests is added', () => {
        const requestsCollection: IRequests[] = [sampleWithRequiredData];
        expectedResult = service.addRequestsToCollectionIfMissing(requestsCollection, undefined, null);
        expect(expectedResult).toEqual(requestsCollection);
      });
    });

    describe('compareRequests', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRequests(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareRequests(entity1, entity2);
        const compareResult2 = service.compareRequests(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareRequests(entity1, entity2);
        const compareResult2 = service.compareRequests(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareRequests(entity1, entity2);
        const compareResult2 = service.compareRequests(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
