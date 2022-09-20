import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRequests, NewRequests } from '../requests.model';

export type PartialUpdateRequests = Partial<IRequests> & Pick<IRequests, 'id'>;

export type EntityResponseType = HttpResponse<IRequests>;
export type EntityArrayResponseType = HttpResponse<IRequests[]>;

@Injectable({ providedIn: 'root' })
export class RequestsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/requests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(requests: NewRequests): Observable<EntityResponseType> {
    return this.http.post<IRequests>(this.resourceUrl, requests, { observe: 'response' });
  }

  update(requests: IRequests): Observable<EntityResponseType> {
    return this.http.put<IRequests>(`${this.resourceUrl}/${this.getRequestsIdentifier(requests)}`, requests, { observe: 'response' });
  }

  partialUpdate(requests: PartialUpdateRequests): Observable<EntityResponseType> {
    return this.http.patch<IRequests>(`${this.resourceUrl}/${this.getRequestsIdentifier(requests)}`, requests, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IRequests>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRequests[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRequestsIdentifier(requests: Pick<IRequests, 'id'>): string {
    return requests.id;
  }

  compareRequests(o1: Pick<IRequests, 'id'> | null, o2: Pick<IRequests, 'id'> | null): boolean {
    return o1 && o2 ? this.getRequestsIdentifier(o1) === this.getRequestsIdentifier(o2) : o1 === o2;
  }

  addRequestsToCollectionIfMissing<Type extends Pick<IRequests, 'id'>>(
    requestsCollection: Type[],
    ...requestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const requests: Type[] = requestsToCheck.filter(isPresent);
    if (requests.length > 0) {
      const requestsCollectionIdentifiers = requestsCollection.map(requestsItem => this.getRequestsIdentifier(requestsItem)!);
      const requestsToAdd = requests.filter(requestsItem => {
        const requestsIdentifier = this.getRequestsIdentifier(requestsItem);
        if (requestsCollectionIdentifiers.includes(requestsIdentifier)) {
          return false;
        }
        requestsCollectionIdentifiers.push(requestsIdentifier);
        return true;
      });
      return [...requestsToAdd, ...requestsCollection];
    }
    return requestsCollection;
  }
}
