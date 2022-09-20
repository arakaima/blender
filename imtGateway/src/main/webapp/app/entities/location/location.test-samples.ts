import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 'e0df5e1a-be08-465b-acc9-eb918c1e21ed',
};

export const sampleWithPartialData: ILocation = {
  id: '6064905d-c622-449b-a006-3140b1a55724',
};

export const sampleWithFullData: ILocation = {
  id: 'e58dffc7-cefd-42e7-802b-ead30f7dcc0f',
};

export const sampleWithNewData: NewLocation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
