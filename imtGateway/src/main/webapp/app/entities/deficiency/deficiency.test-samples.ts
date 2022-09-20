import { IDeficiency, NewDeficiency } from './deficiency.model';

export const sampleWithRequiredData: IDeficiency = {
  id: '7f0ae72f-9ab2-43b5-ac3f-c3a479aba97a',
};

export const sampleWithPartialData: IDeficiency = {
  id: '9f10419b-b074-407f-9ab7-749f0af616cf',
};

export const sampleWithFullData: IDeficiency = {
  id: '3b070bb2-8f89-4ffb-8435-82af91ec4b4c',
};

export const sampleWithNewData: NewDeficiency = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
