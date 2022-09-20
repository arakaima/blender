import { INote, NewNote } from './note.model';

export const sampleWithRequiredData: INote = {
  id: 'c6a38f25-7aea-48a0-9ffb-96501b3ad75f',
};

export const sampleWithPartialData: INote = {
  id: 'a83d6e7e-49e9-43e2-9eaf-b4a89b1c3805',
};

export const sampleWithFullData: INote = {
  id: '3be45530-35f7-4598-8d46-06cbb7cd1a7d',
};

export const sampleWithNewData: NewNote = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
