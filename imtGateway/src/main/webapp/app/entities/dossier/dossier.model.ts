export interface IDossier {
  id: string;
}

export type NewDossier = Omit<IDossier, 'id'> & { id: null };
