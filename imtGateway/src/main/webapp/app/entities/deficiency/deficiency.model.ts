export interface IDeficiency {
  id: string;
}

export type NewDeficiency = Omit<IDeficiency, 'id'> & { id: null };
