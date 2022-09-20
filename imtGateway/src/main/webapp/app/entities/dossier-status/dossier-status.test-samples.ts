import { IDossierStatus, NewDossierStatus } from './dossier-status.model';

export const sampleWithRequiredData: IDossierStatus = {
  id: '53998017-e825-4d5c-9085-de61f5f3ca52',
};

export const sampleWithPartialData: IDossierStatus = {
  id: '2e656999-6e2f-4757-8017-4d0390fb923f',
};

export const sampleWithFullData: IDossierStatus = {
  id: '50c0a8bd-8155-4038-8013-28e3b7af7698',
};

export const sampleWithNewData: NewDossierStatus = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
