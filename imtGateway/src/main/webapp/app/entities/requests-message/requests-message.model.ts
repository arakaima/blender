export interface IRequestsMessage {
  id: string;
}

export type NewRequestsMessage = Omit<IRequestsMessage, 'id'> & { id: null };
