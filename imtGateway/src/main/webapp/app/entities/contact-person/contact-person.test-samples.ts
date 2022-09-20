import { IContactPerson, NewContactPerson } from './contact-person.model';

export const sampleWithRequiredData: IContactPerson = {
  id: 'f63d1090-69f6-4a3a-9144-61f99aff2bf3',
};

export const sampleWithPartialData: IContactPerson = {
  id: '4d89dce8-4fdf-461d-b944-e0b3cfc4ad83',
};

export const sampleWithFullData: IContactPerson = {
  id: '5f926e17-a2a6-4ff0-af01-6bd26d2b62d2',
};

export const sampleWithNewData: NewContactPerson = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
