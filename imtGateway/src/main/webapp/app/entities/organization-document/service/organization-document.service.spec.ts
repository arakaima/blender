import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrganizationDocument } from '../organization-document.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../organization-document.test-samples';

import { OrganizationDocumentService } from './organization-document.service';

const requireRestSample: IOrganizationDocument = {
  ...sampleWithRequiredData,
};

describe('OrganizationDocument Service', () => {
  let service: OrganizationDocumentService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrganizationDocument | IOrganizationDocument[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrganizationDocumentService);
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

    it('should create a OrganizationDocument', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const organizationDocument = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(organizationDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrganizationDocument', () => {
      const organizationDocument = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(organizationDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrganizationDocument', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrganizationDocument', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrganizationDocument', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrganizationDocumentToCollectionIfMissing', () => {
      it('should add a OrganizationDocument to an empty array', () => {
        const organizationDocument: IOrganizationDocument = sampleWithRequiredData;
        expectedResult = service.addOrganizationDocumentToCollectionIfMissing([], organizationDocument);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(organizationDocument);
      });

      it('should not add a OrganizationDocument to an array that contains it', () => {
        const organizationDocument: IOrganizationDocument = sampleWithRequiredData;
        const organizationDocumentCollection: IOrganizationDocument[] = [
          {
            ...organizationDocument,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrganizationDocumentToCollectionIfMissing(organizationDocumentCollection, organizationDocument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrganizationDocument to an array that doesn't contain it", () => {
        const organizationDocument: IOrganizationDocument = sampleWithRequiredData;
        const organizationDocumentCollection: IOrganizationDocument[] = [sampleWithPartialData];
        expectedResult = service.addOrganizationDocumentToCollectionIfMissing(organizationDocumentCollection, organizationDocument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(organizationDocument);
      });

      it('should add only unique OrganizationDocument to an array', () => {
        const organizationDocumentArray: IOrganizationDocument[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const organizationDocumentCollection: IOrganizationDocument[] = [sampleWithRequiredData];
        expectedResult = service.addOrganizationDocumentToCollectionIfMissing(organizationDocumentCollection, ...organizationDocumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const organizationDocument: IOrganizationDocument = sampleWithRequiredData;
        const organizationDocument2: IOrganizationDocument = sampleWithPartialData;
        expectedResult = service.addOrganizationDocumentToCollectionIfMissing([], organizationDocument, organizationDocument2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(organizationDocument);
        expect(expectedResult).toContain(organizationDocument2);
      });

      it('should accept null and undefined values', () => {
        const organizationDocument: IOrganizationDocument = sampleWithRequiredData;
        expectedResult = service.addOrganizationDocumentToCollectionIfMissing([], null, organizationDocument, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(organizationDocument);
      });

      it('should return initial array if no OrganizationDocument is added', () => {
        const organizationDocumentCollection: IOrganizationDocument[] = [sampleWithRequiredData];
        expectedResult = service.addOrganizationDocumentToCollectionIfMissing(organizationDocumentCollection, undefined, null);
        expect(expectedResult).toEqual(organizationDocumentCollection);
      });
    });

    describe('compareOrganizationDocument', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrganizationDocument(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareOrganizationDocument(entity1, entity2);
        const compareResult2 = service.compareOrganizationDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareOrganizationDocument(entity1, entity2);
        const compareResult2 = service.compareOrganizationDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareOrganizationDocument(entity1, entity2);
        const compareResult2 = service.compareOrganizationDocument(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
