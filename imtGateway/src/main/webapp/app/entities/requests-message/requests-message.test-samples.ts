import { IRequestsMessage, NewRequestsMessage } from './requests-message.model';

export const sampleWithRequiredData: IRequestsMessage = {
  id: 'e6164e6d-feca-4a02-a7ec-d73ee6dfca23',
};

export const sampleWithPartialData: IRequestsMessage = {
  id: '84418e21-3469-4d1e-8278-8c4bcbc37e24',
};

export const sampleWithFullData: IRequestsMessage = {
  id: 'ae7069be-b963-4943-b5de-ef2576dac553',
};

export const sampleWithNewData: NewRequestsMessage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
