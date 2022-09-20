export interface IExpert {
  id: string;
}

export type NewExpert = Omit<IExpert, 'id'> & { id: null };
