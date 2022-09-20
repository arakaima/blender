import { IOrganizationDocument, NewOrganizationDocument } from './organization-document.model';

export const sampleWithRequiredData: IOrganizationDocument = {
  id: 'e678596a-ff1c-417f-9fe5-2aa4a12bfd24',
};

export const sampleWithPartialData: IOrganizationDocument = {
  id: '2f6b5923-2970-455f-8793-dab4c111837f',
};

export const sampleWithFullData: IOrganizationDocument = {
  id: '6abab49c-9e25-4ee1-bf1e-65b35ef68442',
};

export const sampleWithNewData: NewOrganizationDocument = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
