import { IInspectionType, NewInspectionType } from './inspection-type.model';

export const sampleWithRequiredData: IInspectionType = {
  id: '449bcf6c-d680-49c0-87e8-cacc516690f7',
};

export const sampleWithPartialData: IInspectionType = {
  id: 'b3851509-973a-4443-8d56-4d66ae7ec602',
};

export const sampleWithFullData: IInspectionType = {
  id: '79af1b24-d256-4003-af2a-a75b8a4ff49c',
};

export const sampleWithNewData: NewInspectionType = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
