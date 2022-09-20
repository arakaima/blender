import { IDossierType, NewDossierType } from './dossier-type.model';

export const sampleWithRequiredData: IDossierType = {
  id: '69e8f7c3-9936-4a13-8a9e-a3fd782e7978',
};

export const sampleWithPartialData: IDossierType = {
  id: '741c85b9-02ff-4bda-b79e-d11afb275fa3',
};

export const sampleWithFullData: IDossierType = {
  id: '4e72b4e0-2f65-48f5-966a-2ad88b0dcf70',
};

export const sampleWithNewData: NewDossierType = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
