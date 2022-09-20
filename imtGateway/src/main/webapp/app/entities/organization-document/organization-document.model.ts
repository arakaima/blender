export interface IOrganizationDocument {
  id: string;
}

export type NewOrganizationDocument = Omit<IOrganizationDocument, 'id'> & { id: null };
