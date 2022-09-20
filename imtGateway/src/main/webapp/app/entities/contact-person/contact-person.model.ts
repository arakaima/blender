export interface IContactPerson {
  id: string;
}

export type NewContactPerson = Omit<IContactPerson, 'id'> & { id: null };
