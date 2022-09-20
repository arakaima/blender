import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRequestsMessage } from '../requests-message.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../requests-message.test-samples';

import { RequestsMessageService } from './requests-message.service';

const requireRestSample: IRequestsMessage = {
  ...sampleWithRequiredData,
};

describe('RequestsMessage Service', () => {
  let service: RequestsMessageService;
  let httpMock: HttpTestingController;
  let expectedResult: IRequestsMessage | IRequestsMessage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RequestsMessageService);
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

    it('should create a RequestsMessage', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const requestsMessage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(requestsMessage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RequestsMessage', () => {
      const requestsMessage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(requestsMessage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RequestsMessage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RequestsMessage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RequestsMessage', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRequestsMessageToCollectionIfMissing', () => {
      it('should add a RequestsMessage to an empty array', () => {
        const requestsMessage: IRequestsMessage = sampleWithRequiredData;
        expectedResult = service.addRequestsMessageToCollectionIfMissing([], requestsMessage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(requestsMessage);
      });

      it('should not add a RequestsMessage to an array that contains it', () => {
        const requestsMessage: IRequestsMessage = sampleWithRequiredData;
        const requestsMessageCollection: IRequestsMessage[] = [
          {
            ...requestsMessage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRequestsMessageToCollectionIfMissing(requestsMessageCollection, requestsMessage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RequestsMessage to an array that doesn't contain it", () => {
        const requestsMessage: IRequestsMessage = sampleWithRequiredData;
        const requestsMessageCollection: IRequestsMessage[] = [sampleWithPartialData];
        expectedResult = service.addRequestsMessageToCollectionIfMissing(requestsMessageCollection, requestsMessage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(requestsMessage);
      });

      it('should add only unique RequestsMessage to an array', () => {
        const requestsMessageArray: IRequestsMessage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const requestsMessageCollection: IRequestsMessage[] = [sampleWithRequiredData];
        expectedResult = service.addRequestsMessageToCollectionIfMissing(requestsMessageCollection, ...requestsMessageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const requestsMessage: IRequestsMessage = sampleWithRequiredData;
        const requestsMessage2: IRequestsMessage = sampleWithPartialData;
        expectedResult = service.addRequestsMessageToCollectionIfMissing([], requestsMessage, requestsMessage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(requestsMessage);
        expect(expectedResult).toContain(requestsMessage2);
      });

      it('should accept null and undefined values', () => {
        const requestsMessage: IRequestsMessage = sampleWithRequiredData;
        expectedResult = service.addRequestsMessageToCollectionIfMissing([], null, requestsMessage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(requestsMessage);
      });

      it('should return initial array if no RequestsMessage is added', () => {
        const requestsMessageCollection: IRequestsMessage[] = [sampleWithRequiredData];
        expectedResult = service.addRequestsMessageToCollectionIfMissing(requestsMessageCollection, undefined, null);
        expect(expectedResult).toEqual(requestsMessageCollection);
      });
    });

    describe('compareRequestsMessage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRequestsMessage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareRequestsMessage(entity1, entity2);
        const compareResult2 = service.compareRequestsMessage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareRequestsMessage(entity1, entity2);
        const compareResult2 = service.compareRequestsMessage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareRequestsMessage(entity1, entity2);
        const compareResult2 = service.compareRequestsMessage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
