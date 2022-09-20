export interface IMessage {
  id: string;
}

export type NewMessage = Omit<IMessage, 'id'> & { id: null };
