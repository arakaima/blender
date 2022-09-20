import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRiskAssessment, NewRiskAssessment } from '../risk-assessment.model';

export type PartialUpdateRiskAssessment = Partial<IRiskAssessment> & Pick<IRiskAssessment, 'id'>;

export type EntityResponseType = HttpResponse<IRiskAssessment>;
export type EntityArrayResponseType = HttpResponse<IRiskAssessment[]>;

@Injectable({ providedIn: 'root' })
export class RiskAssessmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/risk-assessments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(riskAssessment: NewRiskAssessment): Observable<EntityResponseType> {
    return this.http.post<IRiskAssessment>(this.resourceUrl, riskAssessment, { observe: 'response' });
  }

  update(riskAssessment: IRiskAssessment): Observable<EntityResponseType> {
    return this.http.put<IRiskAssessment>(`${this.resourceUrl}/${this.getRiskAssessmentIdentifier(riskAssessment)}`, riskAssessment, {
      observe: 'response',
    });
  }

  partialUpdate(riskAssessment: PartialUpdateRiskAssessment): Observable<EntityResponseType> {
    return this.http.patch<IRiskAssessment>(`${this.resourceUrl}/${this.getRiskAssessmentIdentifier(riskAssessment)}`, riskAssessment, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IRiskAssessment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRiskAssessment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRiskAssessmentIdentifier(riskAssessment: Pick<IRiskAssessment, 'id'>): string {
    return riskAssessment.id;
  }

  compareRiskAssessment(o1: Pick<IRiskAssessment, 'id'> | null, o2: Pick<IRiskAssessment, 'id'> | null): boolean {
    return o1 && o2 ? this.getRiskAssessmentIdentifier(o1) === this.getRiskAssessmentIdentifier(o2) : o1 === o2;
  }

  addRiskAssessmentToCollectionIfMissing<Type extends Pick<IRiskAssessment, 'id'>>(
    riskAssessmentCollection: Type[],
    ...riskAssessmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const riskAssessments: Type[] = riskAssessmentsToCheck.filter(isPresent);
    if (riskAssessments.length > 0) {
      const riskAssessmentCollectionIdentifiers = riskAssessmentCollection.map(
        riskAssessmentItem => this.getRiskAssessmentIdentifier(riskAssessmentItem)!
      );
      const riskAssessmentsToAdd = riskAssessments.filter(riskAssessmentItem => {
        const riskAssessmentIdentifier = this.getRiskAssessmentIdentifier(riskAssessmentItem);
        if (riskAssessmentCollectionIdentifiers.includes(riskAssessmentIdentifier)) {
          return false;
        }
        riskAssessmentCollectionIdentifiers.push(riskAssessmentIdentifier);
        return true;
      });
      return [...riskAssessmentsToAdd, ...riskAssessmentCollection];
    }
    return riskAssessmentCollection;
  }
}
