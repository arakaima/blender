import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICapa } from '../capa.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../capa.test-samples';

import { CapaService } from './capa.service';

const requireRestSample: ICapa = {
  ...sampleWithRequiredData,
};

describe('Capa Service', () => {
  let service: CapaService;
  let httpMock: HttpTestingController;
  let expectedResult: ICapa | ICapa[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CapaService);
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

    it('should create a Capa', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const capa = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(capa).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Capa', () => {
      const capa = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(capa).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Capa', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Capa', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Capa', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCapaToCollectionIfMissing', () => {
      it('should add a Capa to an empty array', () => {
        const capa: ICapa = sampleWithRequiredData;
        expectedResult = service.addCapaToCollectionIfMissing([], capa);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(capa);
      });

      it('should not add a Capa to an array that contains it', () => {
        const capa: ICapa = sampleWithRequiredData;
        const capaCollection: ICapa[] = [
          {
            ...capa,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCapaToCollectionIfMissing(capaCollection, capa);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Capa to an array that doesn't contain it", () => {
        const capa: ICapa = sampleWithRequiredData;
        const capaCollection: ICapa[] = [sampleWithPartialData];
        expectedResult = service.addCapaToCollectionIfMissing(capaCollection, capa);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(capa);
      });

      it('should add only unique Capa to an array', () => {
        const capaArray: ICapa[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const capaCollection: ICapa[] = [sampleWithRequiredData];
        expectedResult = service.addCapaToCollectionIfMissing(capaCollection, ...capaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const capa: ICapa = sampleWithRequiredData;
        const capa2: ICapa = sampleWithPartialData;
        expectedResult = service.addCapaToCollectionIfMissing([], capa, capa2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(capa);
        expect(expectedResult).toContain(capa2);
      });

      it('should accept null and undefined values', () => {
        const capa: ICapa = sampleWithRequiredData;
        expectedResult = service.addCapaToCollectionIfMissing([], null, capa, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(capa);
      });

      it('should return initial array if no Capa is added', () => {
        const capaCollection: ICapa[] = [sampleWithRequiredData];
        expectedResult = service.addCapaToCollectionIfMissing(capaCollection, undefined, null);
        expect(expectedResult).toEqual(capaCollection);
      });
    });

    describe('compareCapa', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCapa(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareCapa(entity1, entity2);
        const compareResult2 = service.compareCapa(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareCapa(entity1, entity2);
        const compareResult2 = service.compareCapa(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareCapa(entity1, entity2);
        const compareResult2 = service.compareCapa(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
