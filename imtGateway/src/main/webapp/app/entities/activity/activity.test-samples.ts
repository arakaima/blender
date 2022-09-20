import { IActivity, NewActivity } from './activity.model';

export const sampleWithRequiredData: IActivity = {
  id: 'a9e28efb-9b0f-4f7d-9b2f-e42de3c4c662',
};

export const sampleWithPartialData: IActivity = {
  id: 'c70b0d04-5d6c-4940-b3e5-505acfb11c31',
};

export const sampleWithFullData: IActivity = {
  id: 'cc5561ac-c350-43a8-887a-95a2c68129e0',
};

export const sampleWithNewData: NewActivity = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
