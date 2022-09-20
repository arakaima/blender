import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttachedDocument, NewAttachedDocument } from '../attached-document.model';

export type PartialUpdateAttachedDocument = Partial<IAttachedDocument> & Pick<IAttachedDocument, 'id'>;

export type EntityResponseType = HttpResponse<IAttachedDocument>;
export type EntityArrayResponseType = HttpResponse<IAttachedDocument[]>;

@Injectable({ providedIn: 'root' })
export class AttachedDocumentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attached-documents');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(attachedDocument: NewAttachedDocument): Observable<EntityResponseType> {
    return this.http.post<IAttachedDocument>(this.resourceUrl, attachedDocument, { observe: 'response' });
  }

  update(attachedDocument: IAttachedDocument): Observable<EntityResponseType> {
    return this.http.put<IAttachedDocument>(
      `${this.resourceUrl}/${this.getAttachedDocumentIdentifier(attachedDocument)}`,
      attachedDocument,
      { observe: 'response' }
    );
  }

  partialUpdate(attachedDocument: PartialUpdateAttachedDocument): Observable<EntityResponseType> {
    return this.http.patch<IAttachedDocument>(
      `${this.resourceUrl}/${this.getAttachedDocumentIdentifier(attachedDocument)}`,
      attachedDocument,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAttachedDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttachedDocument[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAttachedDocumentIdentifier(attachedDocument: Pick<IAttachedDocument, 'id'>): string {
    return attachedDocument.id;
  }

  compareAttachedDocument(o1: Pick<IAttachedDocument, 'id'> | null, o2: Pick<IAttachedDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getAttachedDocumentIdentifier(o1) === this.getAttachedDocumentIdentifier(o2) : o1 === o2;
  }

  addAttachedDocumentToCollectionIfMissing<Type extends Pick<IAttachedDocument, 'id'>>(
    attachedDocumentCollection: Type[],
    ...attachedDocumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const attachedDocuments: Type[] = attachedDocumentsToCheck.filter(isPresent);
    if (attachedDocuments.length > 0) {
      const attachedDocumentCollectionIdentifiers = attachedDocumentCollection.map(
        attachedDocumentItem => this.getAttachedDocumentIdentifier(attachedDocumentItem)!
      );
      const attachedDocumentsToAdd = attachedDocuments.filter(attachedDocumentItem => {
        const attachedDocumentIdentifier = this.getAttachedDocumentIdentifier(attachedDocumentItem);
        if (attachedDocumentCollectionIdentifiers.includes(attachedDocumentIdentifier)) {
          return false;
        }
        attachedDocumentCollectionIdentifiers.push(attachedDocumentIdentifier);
        return true;
      });
      return [...attachedDocumentsToAdd, ...attachedDocumentCollection];
    }
    return attachedDocumentCollection;
  }
}
