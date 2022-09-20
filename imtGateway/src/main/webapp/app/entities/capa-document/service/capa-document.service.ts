import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICapaDocument, NewCapaDocument } from '../capa-document.model';

export type PartialUpdateCapaDocument = Partial<ICapaDocument> & Pick<ICapaDocument, 'id'>;

export type EntityResponseType = HttpResponse<ICapaDocument>;
export type EntityArrayResponseType = HttpResponse<ICapaDocument[]>;

@Injectable({ providedIn: 'root' })
export class CapaDocumentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/capa-documents');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(capaDocument: NewCapaDocument): Observable<EntityResponseType> {
    return this.http.post<ICapaDocument>(this.resourceUrl, capaDocument, { observe: 'response' });
  }

  update(capaDocument: ICapaDocument): Observable<EntityResponseType> {
    return this.http.put<ICapaDocument>(`${this.resourceUrl}/${this.getCapaDocumentIdentifier(capaDocument)}`, capaDocument, {
      observe: 'response',
    });
  }

  partialUpdate(capaDocument: PartialUpdateCapaDocument): Observable<EntityResponseType> {
    return this.http.patch<ICapaDocument>(`${this.resourceUrl}/${this.getCapaDocumentIdentifier(capaDocument)}`, capaDocument, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICapaDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICapaDocument[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCapaDocumentIdentifier(capaDocument: Pick<ICapaDocument, 'id'>): string {
    return capaDocument.id;
  }

  compareCapaDocument(o1: Pick<ICapaDocument, 'id'> | null, o2: Pick<ICapaDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getCapaDocumentIdentifier(o1) === this.getCapaDocumentIdentifier(o2) : o1 === o2;
  }

  addCapaDocumentToCollectionIfMissing<Type extends Pick<ICapaDocument, 'id'>>(
    capaDocumentCollection: Type[],
    ...capaDocumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const capaDocuments: Type[] = capaDocumentsToCheck.filter(isPresent);
    if (capaDocuments.length > 0) {
      const capaDocumentCollectionIdentifiers = capaDocumentCollection.map(
        capaDocumentItem => this.getCapaDocumentIdentifier(capaDocumentItem)!
      );
      const capaDocumentsToAdd = capaDocuments.filter(capaDocumentItem => {
        const capaDocumentIdentifier = this.getCapaDocumentIdentifier(capaDocumentItem);
        if (capaDocumentCollectionIdentifiers.includes(capaDocumentIdentifier)) {
          return false;
        }
        capaDocumentCollectionIdentifiers.push(capaDocumentIdentifier);
        return true;
      });
      return [...capaDocumentsToAdd, ...capaDocumentCollection];
    }
    return capaDocumentCollection;
  }
}
