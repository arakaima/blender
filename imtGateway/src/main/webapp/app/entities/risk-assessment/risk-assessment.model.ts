export interface IRiskAssessment {
  id: string;
}

export type NewRiskAssessment = Omit<IRiskAssessment, 'id'> & { id: null };
