import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInspector } from '../inspector.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../inspector.test-samples';

import { InspectorService } from './inspector.service';

const requireRestSample: IInspector = {
  ...sampleWithRequiredData,
};

describe('Inspector Service', () => {
  let service: InspectorService;
  let httpMock: HttpTestingController;
  let expectedResult: IInspector | IInspector[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InspectorService);
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

    it('should create a Inspector', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const inspector = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inspector).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Inspector', () => {
      const inspector = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inspector).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Inspector', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Inspector', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Inspector', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInspectorToCollectionIfMissing', () => {
      it('should add a Inspector to an empty array', () => {
        const inspector: IInspector = sampleWithRequiredData;
        expectedResult = service.addInspectorToCollectionIfMissing([], inspector);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inspector);
      });

      it('should not add a Inspector to an array that contains it', () => {
        const inspector: IInspector = sampleWithRequiredData;
        const inspectorCollection: IInspector[] = [
          {
            ...inspector,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInspectorToCollectionIfMissing(inspectorCollection, inspector);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Inspector to an array that doesn't contain it", () => {
        const inspector: IInspector = sampleWithRequiredData;
        const inspectorCollection: IInspector[] = [sampleWithPartialData];
        expectedResult = service.addInspectorToCollectionIfMissing(inspectorCollection, inspector);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inspector);
      });

      it('should add only unique Inspector to an array', () => {
        const inspectorArray: IInspector[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const inspectorCollection: IInspector[] = [sampleWithRequiredData];
        expectedResult = service.addInspectorToCollectionIfMissing(inspectorCollection, ...inspectorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inspector: IInspector = sampleWithRequiredData;
        const inspector2: IInspector = sampleWithPartialData;
        expectedResult = service.addInspectorToCollectionIfMissing([], inspector, inspector2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inspector);
        expect(expectedResult).toContain(inspector2);
      });

      it('should accept null and undefined values', () => {
        const inspector: IInspector = sampleWithRequiredData;
        expectedResult = service.addInspectorToCollectionIfMissing([], null, inspector, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inspector);
      });

      it('should return initial array if no Inspector is added', () => {
        const inspectorCollection: IInspector[] = [sampleWithRequiredData];
        expectedResult = service.addInspectorToCollectionIfMissing(inspectorCollection, undefined, null);
        expect(expectedResult).toEqual(inspectorCollection);
      });
    });

    describe('compareInspector', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInspector(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareInspector(entity1, entity2);
        const compareResult2 = service.compareInspector(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareInspector(entity1, entity2);
        const compareResult2 = service.compareInspector(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareInspector(entity1, entity2);
        const compareResult2 = service.compareInspector(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
