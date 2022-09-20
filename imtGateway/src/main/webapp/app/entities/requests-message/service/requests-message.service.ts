import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRequestsMessage, NewRequestsMessage } from '../requests-message.model';

export type PartialUpdateRequestsMessage = Partial<IRequestsMessage> & Pick<IRequestsMessage, 'id'>;

export type EntityResponseType = HttpResponse<IRequestsMessage>;
export type EntityArrayResponseType = HttpResponse<IRequestsMessage[]>;

@Injectable({ providedIn: 'root' })
export class RequestsMessageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/requests-messages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(requestsMessage: NewRequestsMessage): Observable<EntityResponseType> {
    return this.http.post<IRequestsMessage>(this.resourceUrl, requestsMessage, { observe: 'response' });
  }

  update(requestsMessage: IRequestsMessage): Observable<EntityResponseType> {
    return this.http.put<IRequestsMessage>(`${this.resourceUrl}/${this.getRequestsMessageIdentifier(requestsMessage)}`, requestsMessage, {
      observe: 'response',
    });
  }

  partialUpdate(requestsMessage: PartialUpdateRequestsMessage): Observable<EntityResponseType> {
    return this.http.patch<IRequestsMessage>(`${this.resourceUrl}/${this.getRequestsMessageIdentifier(requestsMessage)}`, requestsMessage, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IRequestsMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRequestsMessage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRequestsMessageIdentifier(requestsMessage: Pick<IRequestsMessage, 'id'>): string {
    return requestsMessage.id;
  }

  compareRequestsMessage(o1: Pick<IRequestsMessage, 'id'> | null, o2: Pick<IRequestsMessage, 'id'> | null): boolean {
    return o1 && o2 ? this.getRequestsMessageIdentifier(o1) === this.getRequestsMessageIdentifier(o2) : o1 === o2;
  }

  addRequestsMessageToCollectionIfMissing<Type extends Pick<IRequestsMessage, 'id'>>(
    requestsMessageCollection: Type[],
    ...requestsMessagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const requestsMessages: Type[] = requestsMessagesToCheck.filter(isPresent);
    if (requestsMessages.length > 0) {
      const requestsMessageCollectionIdentifiers = requestsMessageCollection.map(
        requestsMessageItem => this.getRequestsMessageIdentifier(requestsMessageItem)!
      );
      const requestsMessagesToAdd = requestsMessages.filter(requestsMessageItem => {
        const requestsMessageIdentifier = this.getRequestsMessageIdentifier(requestsMessageItem);
        if (requestsMessageCollectionIdentifiers.includes(requestsMessageIdentifier)) {
          return false;
        }
        requestsMessageCollectionIdentifiers.push(requestsMessageIdentifier);
        return true;
      });
      return [...requestsMessagesToAdd, ...requestsMessageCollection];
    }
    return requestsMessageCollection;
  }
}
