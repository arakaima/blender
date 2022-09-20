export interface ISite {
  id: string;
}

export type NewSite = Omit<ISite, 'id'> & { id: null };
