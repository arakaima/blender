import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRiskAssessment } from '../risk-assessment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../risk-assessment.test-samples';

import { RiskAssessmentService } from './risk-assessment.service';

const requireRestSample: IRiskAssessment = {
  ...sampleWithRequiredData,
};

describe('RiskAssessment Service', () => {
  let service: RiskAssessmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IRiskAssessment | IRiskAssessment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RiskAssessmentService);
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

    it('should create a RiskAssessment', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const riskAssessment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(riskAssessment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RiskAssessment', () => {
      const riskAssessment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(riskAssessment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RiskAssessment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RiskAssessment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RiskAssessment', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRiskAssessmentToCollectionIfMissing', () => {
      it('should add a RiskAssessment to an empty array', () => {
        const riskAssessment: IRiskAssessment = sampleWithRequiredData;
        expectedResult = service.addRiskAssessmentToCollectionIfMissing([], riskAssessment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(riskAssessment);
      });

      it('should not add a RiskAssessment to an array that contains it', () => {
        const riskAssessment: IRiskAssessment = sampleWithRequiredData;
        const riskAssessmentCollection: IRiskAssessment[] = [
          {
            ...riskAssessment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRiskAssessmentToCollectionIfMissing(riskAssessmentCollection, riskAssessment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RiskAssessment to an array that doesn't contain it", () => {
        const riskAssessment: IRiskAssessment = sampleWithRequiredData;
        const riskAssessmentCollection: IRiskAssessment[] = [sampleWithPartialData];
        expectedResult = service.addRiskAssessmentToCollectionIfMissing(riskAssessmentCollection, riskAssessment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(riskAssessment);
      });

      it('should add only unique RiskAssessment to an array', () => {
        const riskAssessmentArray: IRiskAssessment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const riskAssessmentCollection: IRiskAssessment[] = [sampleWithRequiredData];
        expectedResult = service.addRiskAssessmentToCollectionIfMissing(riskAssessmentCollection, ...riskAssessmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const riskAssessment: IRiskAssessment = sampleWithRequiredData;
        const riskAssessment2: IRiskAssessment = sampleWithPartialData;
        expectedResult = service.addRiskAssessmentToCollectionIfMissing([], riskAssessment, riskAssessment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(riskAssessment);
        expect(expectedResult).toContain(riskAssessment2);
      });

      it('should accept null and undefined values', () => {
        const riskAssessment: IRiskAssessment = sampleWithRequiredData;
        expectedResult = service.addRiskAssessmentToCollectionIfMissing([], null, riskAssessment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(riskAssessment);
      });

      it('should return initial array if no RiskAssessment is added', () => {
        const riskAssessmentCollection: IRiskAssessment[] = [sampleWithRequiredData];
        expectedResult = service.addRiskAssessmentToCollectionIfMissing(riskAssessmentCollection, undefined, null);
        expect(expectedResult).toEqual(riskAssessmentCollection);
      });
    });

    describe('compareRiskAssessment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRiskAssessment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareRiskAssessment(entity1, entity2);
        const compareResult2 = service.compareRiskAssessment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareRiskAssessment(entity1, entity2);
        const compareResult2 = service.compareRiskAssessment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareRiskAssessment(entity1, entity2);
        const compareResult2 = service.compareRiskAssessment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
