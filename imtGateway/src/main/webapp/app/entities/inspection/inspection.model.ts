export interface IInspection {
  id: string;
}

export type NewInspection = Omit<IInspection, 'id'> & { id: null };
