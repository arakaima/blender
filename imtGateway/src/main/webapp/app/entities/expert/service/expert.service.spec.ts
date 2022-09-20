import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IExpert } from '../expert.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../expert.test-samples';

import { ExpertService } from './expert.service';

const requireRestSample: IExpert = {
  ...sampleWithRequiredData,
};

describe('Expert Service', () => {
  let service: ExpertService;
  let httpMock: HttpTestingController;
  let expectedResult: IExpert | IExpert[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExpertService);
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

    it('should create a Expert', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const expert = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(expert).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Expert', () => {
      const expert = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(expert).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Expert', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Expert', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Expert', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExpertToCollectionIfMissing', () => {
      it('should add a Expert to an empty array', () => {
        const expert: IExpert = sampleWithRequiredData;
        expectedResult = service.addExpertToCollectionIfMissing([], expert);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expert);
      });

      it('should not add a Expert to an array that contains it', () => {
        const expert: IExpert = sampleWithRequiredData;
        const expertCollection: IExpert[] = [
          {
            ...expert,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExpertToCollectionIfMissing(expertCollection, expert);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Expert to an array that doesn't contain it", () => {
        const expert: IExpert = sampleWithRequiredData;
        const expertCollection: IExpert[] = [sampleWithPartialData];
        expectedResult = service.addExpertToCollectionIfMissing(expertCollection, expert);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expert);
      });

      it('should add only unique Expert to an array', () => {
        const expertArray: IExpert[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const expertCollection: IExpert[] = [sampleWithRequiredData];
        expectedResult = service.addExpertToCollectionIfMissing(expertCollection, ...expertArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const expert: IExpert = sampleWithRequiredData;
        const expert2: IExpert = sampleWithPartialData;
        expectedResult = service.addExpertToCollectionIfMissing([], expert, expert2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expert);
        expect(expectedResult).toContain(expert2);
      });

      it('should accept null and undefined values', () => {
        const expert: IExpert = sampleWithRequiredData;
        expectedResult = service.addExpertToCollectionIfMissing([], null, expert, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expert);
      });

      it('should return initial array if no Expert is added', () => {
        const expertCollection: IExpert[] = [sampleWithRequiredData];
        expectedResult = service.addExpertToCollectionIfMissing(expertCollection, undefined, null);
        expect(expectedResult).toEqual(expertCollection);
      });
    });

    describe('compareExpert', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExpert(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareExpert(entity1, entity2);
        const compareResult2 = service.compareExpert(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareExpert(entity1, entity2);
        const compareResult2 = service.compareExpert(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareExpert(entity1, entity2);
        const compareResult2 = service.compareExpert(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
