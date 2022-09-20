export interface ICapa {
  id: string;
}

export type NewCapa = Omit<ICapa, 'id'> & { id: null };
