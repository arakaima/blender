export interface IInspectionType {
  id: string;
}

export type NewInspectionType = Omit<IInspectionType, 'id'> & { id: null };
