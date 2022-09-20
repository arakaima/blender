import { IOrganization, NewOrganization } from './organization.model';

export const sampleWithRequiredData: IOrganization = {
  id: '2930cbfe-8939-4d04-9905-c946aeca56ad',
};

export const sampleWithPartialData: IOrganization = {
  id: 'aac2faa9-2be0-4d33-baaf-03904669c02f',
};

export const sampleWithFullData: IOrganization = {
  id: '01aa6aa5-c07e-4d22-bc49-af7042358fee',
};

export const sampleWithNewData: NewOrganization = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
