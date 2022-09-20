import { IExpert, NewExpert } from './expert.model';

export const sampleWithRequiredData: IExpert = {
  id: '6f095bec-d59c-4be5-b36f-a3c89bce7868',
};

export const sampleWithPartialData: IExpert = {
  id: '605e5e78-437f-4351-b126-afba357b9fa5',
};

export const sampleWithFullData: IExpert = {
  id: '3974efc7-4945-4fc6-83eb-c5dbe84db760',
};

export const sampleWithNewData: NewExpert = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
