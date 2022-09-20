export interface ILocation {
  id: string;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };
