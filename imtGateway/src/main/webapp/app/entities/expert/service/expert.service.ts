import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExpert, NewExpert } from '../expert.model';

export type PartialUpdateExpert = Partial<IExpert> & Pick<IExpert, 'id'>;

export type EntityResponseType = HttpResponse<IExpert>;
export type EntityArrayResponseType = HttpResponse<IExpert[]>;

@Injectable({ providedIn: 'root' })
export class ExpertService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/experts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(expert: NewExpert): Observable<EntityResponseType> {
    return this.http.post<IExpert>(this.resourceUrl, expert, { observe: 'response' });
  }

  update(expert: IExpert): Observable<EntityResponseType> {
    return this.http.put<IExpert>(`${this.resourceUrl}/${this.getExpertIdentifier(expert)}`, expert, { observe: 'response' });
  }

  partialUpdate(expert: PartialUpdateExpert): Observable<EntityResponseType> {
    return this.http.patch<IExpert>(`${this.resourceUrl}/${this.getExpertIdentifier(expert)}`, expert, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IExpert>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExpert[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExpertIdentifier(expert: Pick<IExpert, 'id'>): string {
    return expert.id;
  }

  compareExpert(o1: Pick<IExpert, 'id'> | null, o2: Pick<IExpert, 'id'> | null): boolean {
    return o1 && o2 ? this.getExpertIdentifier(o1) === this.getExpertIdentifier(o2) : o1 === o2;
  }

  addExpertToCollectionIfMissing<Type extends Pick<IExpert, 'id'>>(
    expertCollection: Type[],
    ...expertsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const experts: Type[] = expertsToCheck.filter(isPresent);
    if (experts.length > 0) {
      const expertCollectionIdentifiers = expertCollection.map(expertItem => this.getExpertIdentifier(expertItem)!);
      const expertsToAdd = experts.filter(expertItem => {
        const expertIdentifier = this.getExpertIdentifier(expertItem);
        if (expertCollectionIdentifiers.includes(expertIdentifier)) {
          return false;
        }
        expertCollectionIdentifiers.push(expertIdentifier);
        return true;
      });
      return [...expertsToAdd, ...expertCollection];
    }
    return expertCollection;
  }
}
