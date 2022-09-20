import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: '4f5a3d40-9bf6-4ec2-ae8d-95e9265434c5',
};

export const sampleWithPartialData: IMessage = {
  id: 'daac853e-8593-472c-9be1-26e39af77f10',
};

export const sampleWithFullData: IMessage = {
  id: 'bdfc9ee8-6a88-4fde-85ed-c0a8d0412e2f',
};

export const sampleWithNewData: NewMessage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
