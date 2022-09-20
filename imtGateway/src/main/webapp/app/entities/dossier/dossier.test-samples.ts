import { IDossier, NewDossier } from './dossier.model';

export const sampleWithRequiredData: IDossier = {
  id: '57416065-bac8-468e-bc81-78b4dd033d88',
};

export const sampleWithPartialData: IDossier = {
  id: '972c58e0-8072-4ec1-9a0c-c98e1abe4584',
};

export const sampleWithFullData: IDossier = {
  id: 'b1cb686a-aa0b-4ee9-8354-da6bd22db108',
};

export const sampleWithNewData: NewDossier = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
