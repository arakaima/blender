export interface IDossierStatus {
  id: string;
}

export type NewDossierStatus = Omit<IDossierStatus, 'id'> & { id: null };
