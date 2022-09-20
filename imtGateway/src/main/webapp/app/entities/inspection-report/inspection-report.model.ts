export interface IInspectionReport {
  id: string;
}

export type NewInspectionReport = Omit<IInspectionReport, 'id'> & { id: null };
