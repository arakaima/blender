import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeficiency } from '../deficiency.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../deficiency.test-samples';

import { DeficiencyService } from './deficiency.service';

const requireRestSample: IDeficiency = {
  ...sampleWithRequiredData,
};

describe('Deficiency Service', () => {
  let service: DeficiencyService;
  let httpMock: HttpTestingController;
  let expectedResult: IDeficiency | IDeficiency[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeficiencyService);
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

    it('should create a Deficiency', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const deficiency = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(deficiency).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Deficiency', () => {
      const deficiency = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(deficiency).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Deficiency', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Deficiency', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Deficiency', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDeficiencyToCollectionIfMissing', () => {
      it('should add a Deficiency to an empty array', () => {
        const deficiency: IDeficiency = sampleWithRequiredData;
        expectedResult = service.addDeficiencyToCollectionIfMissing([], deficiency);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deficiency);
      });

      it('should not add a Deficiency to an array that contains it', () => {
        const deficiency: IDeficiency = sampleWithRequiredData;
        const deficiencyCollection: IDeficiency[] = [
          {
            ...deficiency,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDeficiencyToCollectionIfMissing(deficiencyCollection, deficiency);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Deficiency to an array that doesn't contain it", () => {
        const deficiency: IDeficiency = sampleWithRequiredData;
        const deficiencyCollection: IDeficiency[] = [sampleWithPartialData];
        expectedResult = service.addDeficiencyToCollectionIfMissing(deficiencyCollection, deficiency);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deficiency);
      });

      it('should add only unique Deficiency to an array', () => {
        const deficiencyArray: IDeficiency[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const deficiencyCollection: IDeficiency[] = [sampleWithRequiredData];
        expectedResult = service.addDeficiencyToCollectionIfMissing(deficiencyCollection, ...deficiencyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deficiency: IDeficiency = sampleWithRequiredData;
        const deficiency2: IDeficiency = sampleWithPartialData;
        expectedResult = service.addDeficiencyToCollectionIfMissing([], deficiency, deficiency2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deficiency);
        expect(expectedResult).toContain(deficiency2);
      });

      it('should accept null and undefined values', () => {
        const deficiency: IDeficiency = sampleWithRequiredData;
        expectedResult = service.addDeficiencyToCollectionIfMissing([], null, deficiency, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deficiency);
      });

      it('should return initial array if no Deficiency is added', () => {
        const deficiencyCollection: IDeficiency[] = [sampleWithRequiredData];
        expectedResult = service.addDeficiencyToCollectionIfMissing(deficiencyCollection, undefined, null);
        expect(expectedResult).toEqual(deficiencyCollection);
      });
    });

    describe('compareDeficiency', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDeficiency(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareDeficiency(entity1, entity2);
        const compareResult2 = service.compareDeficiency(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareDeficiency(entity1, entity2);
        const compareResult2 = service.compareDeficiency(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareDeficiency(entity1, entity2);
        const compareResult2 = service.compareDeficiency(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
