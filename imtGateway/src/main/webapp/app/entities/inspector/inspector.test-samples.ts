import { IInspector, NewInspector } from './inspector.model';

export const sampleWithRequiredData: IInspector = {
  id: 'b2ce6e63-ad15-46ed-af06-6322d5c5206b',
};

export const sampleWithPartialData: IInspector = {
  id: '5a02a157-37fc-4318-b418-b7800fe07136',
};

export const sampleWithFullData: IInspector = {
  id: '0842194f-5eaf-490f-b12a-9602031b4a93',
};

export const sampleWithNewData: NewInspector = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
