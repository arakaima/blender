import { IAttachedDocument, NewAttachedDocument } from './attached-document.model';

export const sampleWithRequiredData: IAttachedDocument = {
  id: 'f277551f-efe3-4cbe-986f-49898f6775a4',
};

export const sampleWithPartialData: IAttachedDocument = {
  id: '7668fb8d-0f16-408e-aa69-e43d4510874e',
};

export const sampleWithFullData: IAttachedDocument = {
  id: '4d1d21b1-7e29-42e0-8246-9978213bbd14',
};

export const sampleWithNewData: NewAttachedDocument = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
