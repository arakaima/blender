import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDossierType } from '../dossier-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../dossier-type.test-samples';

import { DossierTypeService } from './dossier-type.service';

const requireRestSample: IDossierType = {
  ...sampleWithRequiredData,
};

describe('DossierType Service', () => {
  let service: DossierTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IDossierType | IDossierType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DossierTypeService);
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

    it('should create a DossierType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const dossierType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(dossierType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DossierType', () => {
      const dossierType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(dossierType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DossierType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DossierType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DossierType', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDossierTypeToCollectionIfMissing', () => {
      it('should add a DossierType to an empty array', () => {
        const dossierType: IDossierType = sampleWithRequiredData;
        expectedResult = service.addDossierTypeToCollectionIfMissing([], dossierType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dossierType);
      });

      it('should not add a DossierType to an array that contains it', () => {
        const dossierType: IDossierType = sampleWithRequiredData;
        const dossierTypeCollection: IDossierType[] = [
          {
            ...dossierType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDossierTypeToCollectionIfMissing(dossierTypeCollection, dossierType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DossierType to an array that doesn't contain it", () => {
        const dossierType: IDossierType = sampleWithRequiredData;
        const dossierTypeCollection: IDossierType[] = [sampleWithPartialData];
        expectedResult = service.addDossierTypeToCollectionIfMissing(dossierTypeCollection, dossierType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dossierType);
      });

      it('should add only unique DossierType to an array', () => {
        const dossierTypeArray: IDossierType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const dossierTypeCollection: IDossierType[] = [sampleWithRequiredData];
        expectedResult = service.addDossierTypeToCollectionIfMissing(dossierTypeCollection, ...dossierTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dossierType: IDossierType = sampleWithRequiredData;
        const dossierType2: IDossierType = sampleWithPartialData;
        expectedResult = service.addDossierTypeToCollectionIfMissing([], dossierType, dossierType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dossierType);
        expect(expectedResult).toContain(dossierType2);
      });

      it('should accept null and undefined values', () => {
        const dossierType: IDossierType = sampleWithRequiredData;
        expectedResult = service.addDossierTypeToCollectionIfMissing([], null, dossierType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dossierType);
      });

      it('should return initial array if no DossierType is added', () => {
        const dossierTypeCollection: IDossierType[] = [sampleWithRequiredData];
        expectedResult = service.addDossierTypeToCollectionIfMissing(dossierTypeCollection, undefined, null);
        expect(expectedResult).toEqual(dossierTypeCollection);
      });
    });

    describe('compareDossierType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDossierType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareDossierType(entity1, entity2);
        const compareResult2 = service.compareDossierType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareDossierType(entity1, entity2);
        const compareResult2 = service.compareDossierType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareDossierType(entity1, entity2);
        const compareResult2 = service.compareDossierType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
