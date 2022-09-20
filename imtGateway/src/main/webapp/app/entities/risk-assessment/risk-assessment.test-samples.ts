import { IRiskAssessment, NewRiskAssessment } from './risk-assessment.model';

export const sampleWithRequiredData: IRiskAssessment = {
  id: 'a6a8b697-c84b-49c6-944f-3f894cd6c7f5',
};

export const sampleWithPartialData: IRiskAssessment = {
  id: 'dd064fe8-a024-4b5f-9d76-e5bd5855cd68',
};

export const sampleWithFullData: IRiskAssessment = {
  id: '7f9f9b36-d013-4f8b-bd67-1cda585acc4e',
};

export const sampleWithNewData: NewRiskAssessment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
