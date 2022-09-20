export interface IInspectorDossier {
  id: string;
}

export type NewInspectorDossier = Omit<IInspectorDossier, 'id'> & { id: null };
