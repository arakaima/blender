import { IInspectionReport, NewInspectionReport } from './inspection-report.model';

export const sampleWithRequiredData: IInspectionReport = {
  id: '8828a98e-7265-476b-aded-949436902bca',
};

export const sampleWithPartialData: IInspectionReport = {
  id: '1982656d-b205-435e-a1dd-6092e3204683',
};

export const sampleWithFullData: IInspectionReport = {
  id: '91488549-335b-4f57-bac3-fe3a0be010e5',
};

export const sampleWithNewData: NewInspectionReport = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
