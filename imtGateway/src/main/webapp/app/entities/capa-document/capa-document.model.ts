export interface ICapaDocument {
  id: string;
}

export type NewCapaDocument = Omit<ICapaDocument, 'id'> & { id: null };
