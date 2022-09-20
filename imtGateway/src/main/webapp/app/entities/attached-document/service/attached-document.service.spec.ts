import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAttachedDocument } from '../attached-document.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../attached-document.test-samples';

import { AttachedDocumentService } from './attached-document.service';

const requireRestSample: IAttachedDocument = {
  ...sampleWithRequiredData,
};

describe('AttachedDocument Service', () => {
  let service: AttachedDocumentService;
  let httpMock: HttpTestingController;
  let expectedResult: IAttachedDocument | IAttachedDocument[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AttachedDocumentService);
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

    it('should create a AttachedDocument', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const attachedDocument = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(attachedDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AttachedDocument', () => {
      const attachedDocument = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(attachedDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AttachedDocument', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AttachedDocument', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AttachedDocument', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAttachedDocumentToCollectionIfMissing', () => {
      it('should add a AttachedDocument to an empty array', () => {
        const attachedDocument: IAttachedDocument = sampleWithRequiredData;
        expectedResult = service.addAttachedDocumentToCollectionIfMissing([], attachedDocument);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attachedDocument);
      });

      it('should not add a AttachedDocument to an array that contains it', () => {
        const attachedDocument: IAttachedDocument = sampleWithRequiredData;
        const attachedDocumentCollection: IAttachedDocument[] = [
          {
            ...attachedDocument,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAttachedDocumentToCollectionIfMissing(attachedDocumentCollection, attachedDocument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AttachedDocument to an array that doesn't contain it", () => {
        const attachedDocument: IAttachedDocument = sampleWithRequiredData;
        const attachedDocumentCollection: IAttachedDocument[] = [sampleWithPartialData];
        expectedResult = service.addAttachedDocumentToCollectionIfMissing(attachedDocumentCollection, attachedDocument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attachedDocument);
      });

      it('should add only unique AttachedDocument to an array', () => {
        const attachedDocumentArray: IAttachedDocument[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const attachedDocumentCollection: IAttachedDocument[] = [sampleWithRequiredData];
        expectedResult = service.addAttachedDocumentToCollectionIfMissing(attachedDocumentCollection, ...attachedDocumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const attachedDocument: IAttachedDocument = sampleWithRequiredData;
        const attachedDocument2: IAttachedDocument = sampleWithPartialData;
        expectedResult = service.addAttachedDocumentToCollectionIfMissing([], attachedDocument, attachedDocument2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attachedDocument);
        expect(expectedResult).toContain(attachedDocument2);
      });

      it('should accept null and undefined values', () => {
        const attachedDocument: IAttachedDocument = sampleWithRequiredData;
        expectedResult = service.addAttachedDocumentToCollectionIfMissing([], null, attachedDocument, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attachedDocument);
      });

      it('should return initial array if no AttachedDocument is added', () => {
        const attachedDocumentCollection: IAttachedDocument[] = [sampleWithRequiredData];
        expectedResult = service.addAttachedDocumentToCollectionIfMissing(attachedDocumentCollection, undefined, null);
        expect(expectedResult).toEqual(attachedDocumentCollection);
      });
    });

    describe('compareAttachedDocument', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAttachedDocument(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareAttachedDocument(entity1, entity2);
        const compareResult2 = service.compareAttachedDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareAttachedDocument(entity1, entity2);
        const compareResult2 = service.compareAttachedDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareAttachedDocument(entity1, entity2);
        const compareResult2 = service.compareAttachedDocument(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
