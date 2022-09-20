export interface IInspector {
  id: string;
}

export type NewInspector = Omit<IInspector, 'id'> & { id: null };
