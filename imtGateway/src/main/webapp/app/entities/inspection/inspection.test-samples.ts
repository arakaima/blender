import { IInspection, NewInspection } from './inspection.model';

export const sampleWithRequiredData: IInspection = {
  id: '157a95d2-8d7c-431c-95bd-160f176fb316',
};

export const sampleWithPartialData: IInspection = {
  id: '6501f088-d744-4304-bb86-23e504fbe47b',
};

export const sampleWithFullData: IInspection = {
  id: 'd59a2689-0062-4c7a-ae56-88b88e7612e4',
};

export const sampleWithNewData: NewInspection = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
