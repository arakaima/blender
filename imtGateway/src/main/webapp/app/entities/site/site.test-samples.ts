import { ISite, NewSite } from './site.model';

export const sampleWithRequiredData: ISite = {
  id: 'b277326f-86ac-4174-b7cb-5ac389d74c99',
};

export const sampleWithPartialData: ISite = {
  id: '32a508bf-247e-40f7-b68c-a66b7e0e0f95',
};

export const sampleWithFullData: ISite = {
  id: '849ca9c4-ab48-4c39-afcf-2ca4bb09fd6c',
};

export const sampleWithNewData: NewSite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
