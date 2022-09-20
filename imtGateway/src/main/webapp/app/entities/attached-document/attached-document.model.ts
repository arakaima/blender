export interface IAttachedDocument {
  id: string;
}

export type NewAttachedDocument = Omit<IAttachedDocument, 'id'> & { id: null };
