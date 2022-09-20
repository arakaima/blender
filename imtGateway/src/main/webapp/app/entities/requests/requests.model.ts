export interface IRequests {
  id: string;
}

export type NewRequests = Omit<IRequests, 'id'> & { id: null };
