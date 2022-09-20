import { ICapaDocument, NewCapaDocument } from './capa-document.model';

export const sampleWithRequiredData: ICapaDocument = {
  id: 'b3c9fbbf-2278-477e-819c-8b9135846ac2',
};

export const sampleWithPartialData: ICapaDocument = {
  id: 'b0ac1c9b-ba2d-4307-8dbe-c20cfe0c251e',
};

export const sampleWithFullData: ICapaDocument = {
  id: '9b76ef75-b460-4daa-803c-10cac53336e6',
};

export const sampleWithNewData: NewCapaDocument = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
