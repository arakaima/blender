export interface IActivity {
  id: string;
}

export type NewActivity = Omit<IActivity, 'id'> & { id: null };
