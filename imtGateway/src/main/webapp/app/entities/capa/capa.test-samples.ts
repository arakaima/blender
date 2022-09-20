import { ICapa, NewCapa } from './capa.model';

export const sampleWithRequiredData: ICapa = {
  id: 'c8829847-1adc-4058-bccb-c47c7f488ce4',
};

export const sampleWithPartialData: ICapa = {
  id: '2b39c465-d12f-4e5f-862b-f39ed6b356e4',
};

export const sampleWithFullData: ICapa = {
  id: 'f25f959e-294c-41d8-901a-10db872e9dff',
};

export const sampleWithNewData: NewCapa = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
