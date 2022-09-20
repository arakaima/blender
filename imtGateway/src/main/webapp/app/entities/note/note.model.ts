export interface INote {
  id: string;
}

export type NewNote = Omit<INote, 'id'> & { id: null };
