import { IRequests, NewRequests } from './requests.model';

export const sampleWithRequiredData: IRequests = {
  id: 'eb16f765-c715-484e-8a86-e6810c13575b',
};

export const sampleWithPartialData: IRequests = {
  id: '8c4878af-2425-4463-8768-414fed142f79',
};

export const sampleWithFullData: IRequests = {
  id: '165d3231-0e3a-4768-be0b-7dcf23e5baea',
};

export const sampleWithNewData: NewRequests = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
