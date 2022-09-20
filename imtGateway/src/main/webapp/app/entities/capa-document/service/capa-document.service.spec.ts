import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICapaDocument } from '../capa-document.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../capa-document.test-samples';

import { CapaDocumentService } from './capa-document.service';

const requireRestSample: ICapaDocument = {
  ...sampleWithRequiredData,
};

describe('CapaDocument Service', () => {
  let service: CapaDocumentService;
  let httpMock: HttpTestingController;
  let expectedResult: ICapaDocument | ICapaDocument[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CapaDocumentService);
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

    it('should create a CapaDocument', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const capaDocument = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(capaDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CapaDocument', () => {
      const capaDocument = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(capaDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CapaDocument', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CapaDocument', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CapaDocument', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCapaDocumentToCollectionIfMissing', () => {
      it('should add a CapaDocument to an empty array', () => {
        const capaDocument: ICapaDocument = sampleWithRequiredData;
        expectedResult = service.addCapaDocumentToCollectionIfMissing([], capaDocument);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(capaDocument);
      });

      it('should not add a CapaDocument to an array that contains it', () => {
        const capaDocument: ICapaDocument = sampleWithRequiredData;
        const capaDocumentCollection: ICapaDocument[] = [
          {
            ...capaDocument,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCapaDocumentToCollectionIfMissing(capaDocumentCollection, capaDocument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CapaDocument to an array that doesn't contain it", () => {
        const capaDocument: ICapaDocument = sampleWithRequiredData;
        const capaDocumentCollection: ICapaDocument[] = [sampleWithPartialData];
        expectedResult = service.addCapaDocumentToCollectionIfMissing(capaDocumentCollection, capaDocument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(capaDocument);
      });

      it('should add only unique CapaDocument to an array', () => {
        const capaDocumentArray: ICapaDocument[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const capaDocumentCollection: ICapaDocument[] = [sampleWithRequiredData];
        expectedResult = service.addCapaDocumentToCollectionIfMissing(capaDocumentCollection, ...capaDocumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const capaDocument: ICapaDocument = sampleWithRequiredData;
        const capaDocument2: ICapaDocument = sampleWithPartialData;
        expectedResult = service.addCapaDocumentToCollectionIfMissing([], capaDocument, capaDocument2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(capaDocument);
        expect(expectedResult).toContain(capaDocument2);
      });

      it('should accept null and undefined values', () => {
        const capaDocument: ICapaDocument = sampleWithRequiredData;
        expectedResult = service.addCapaDocumentToCollectionIfMissing([], null, capaDocument, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(capaDocument);
      });

      it('should return initial array if no CapaDocument is added', () => {
        const capaDocumentCollection: ICapaDocument[] = [sampleWithRequiredData];
        expectedResult = service.addCapaDocumentToCollectionIfMissing(capaDocumentCollection, undefined, null);
        expect(expectedResult).toEqual(capaDocumentCollection);
      });
    });

    describe('compareCapaDocument', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCapaDocument(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareCapaDocument(entity1, entity2);
        const compareResult2 = service.compareCapaDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareCapaDocument(entity1, entity2);
        const compareResult2 = service.compareCapaDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareCapaDocument(entity1, entity2);
        const compareResult2 = service.compareCapaDocument(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
