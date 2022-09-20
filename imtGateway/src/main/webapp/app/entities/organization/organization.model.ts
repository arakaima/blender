export interface IOrganization {
  id: string;
}

export type NewOrganization = Omit<IOrganization, 'id'> & { id: null };
