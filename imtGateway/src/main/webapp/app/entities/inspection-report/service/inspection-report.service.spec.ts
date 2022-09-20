import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInspectionReport } from '../inspection-report.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../inspection-report.test-samples';

import { InspectionReportService } from './inspection-report.service';

const requireRestSample: IInspectionReport = {
  ...sampleWithRequiredData,
};

describe('InspectionReport Service', () => {
  let service: InspectionReportService;
  let httpMock: HttpTestingController;
  let expectedResult: IInspectionReport | IInspectionReport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InspectionReportService);
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

    it('should create a InspectionReport', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const inspectionReport = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inspectionReport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InspectionReport', () => {
      const inspectionReport = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inspectionReport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InspectionReport', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InspectionReport', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InspectionReport', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInspectionReportToCollectionIfMissing', () => {
      it('should add a InspectionReport to an empty array', () => {
        const inspectionReport: IInspectionReport = sampleWithRequiredData;
        expectedResult = service.addInspectionReportToCollectionIfMissing([], inspectionReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inspectionReport);
      });

      it('should not add a InspectionReport to an array that contains it', () => {
        const inspectionReport: IInspectionReport = sampleWithRequiredData;
        const inspectionReportCollection: IInspectionReport[] = [
          {
            ...inspectionReport,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInspectionReportToCollectionIfMissing(inspectionReportCollection, inspectionReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InspectionReport to an array that doesn't contain it", () => {
        const inspectionReport: IInspectionReport = sampleWithRequiredData;
        const inspectionReportCollection: IInspectionReport[] = [sampleWithPartialData];
        expectedResult = service.addInspectionReportToCollectionIfMissing(inspectionReportCollection, inspectionReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inspectionReport);
      });

      it('should add only unique InspectionReport to an array', () => {
        const inspectionReportArray: IInspectionReport[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const inspectionReportCollection: IInspectionReport[] = [sampleWithRequiredData];
        expectedResult = service.addInspectionReportToCollectionIfMissing(inspectionReportCollection, ...inspectionReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inspectionReport: IInspectionReport = sampleWithRequiredData;
        const inspectionReport2: IInspectionReport = sampleWithPartialData;
        expectedResult = service.addInspectionReportToCollectionIfMissing([], inspectionReport, inspectionReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inspectionReport);
        expect(expectedResult).toContain(inspectionReport2);
      });

      it('should accept null and undefined values', () => {
        const inspectionReport: IInspectionReport = sampleWithRequiredData;
        expectedResult = service.addInspectionReportToCollectionIfMissing([], null, inspectionReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inspectionReport);
      });

      it('should return initial array if no InspectionReport is added', () => {
        const inspectionReportCollection: IInspectionReport[] = [sampleWithRequiredData];
        expectedResult = service.addInspectionReportToCollectionIfMissing(inspectionReportCollection, undefined, null);
        expect(expectedResult).toEqual(inspectionReportCollection);
      });
    });

    describe('compareInspectionReport', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInspectionReport(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareInspectionReport(entity1, entity2);
        const compareResult2 = service.compareInspectionReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareInspectionReport(entity1, entity2);
        const compareResult2 = service.compareInspectionReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareInspectionReport(entity1, entity2);
        const compareResult2 = service.compareInspectionReport(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
