export interface IDossierType {
  id: string;
}

export type NewDossierType = Omit<IDossierType, 'id'> & { id: null };
