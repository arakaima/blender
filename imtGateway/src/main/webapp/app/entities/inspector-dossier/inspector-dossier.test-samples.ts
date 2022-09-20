import { IInspectorDossier, NewInspectorDossier } from './inspector-dossier.model';

export const sampleWithRequiredData: IInspectorDossier = {
  id: '6a6a687d-b1ed-4ce0-b490-4c44f36bdbfb',
};

export const sampleWithPartialData: IInspectorDossier = {
  id: '1d13fe11-cd5b-4070-9726-6c701da78ca8',
};

export const sampleWithFullData: IInspectorDossier = {
  id: '04d6d438-2300-4373-931b-840806904e61',
};

export const sampleWithNewData: NewInspectorDossier = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
