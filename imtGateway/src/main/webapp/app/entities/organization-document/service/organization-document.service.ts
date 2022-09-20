import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganizationDocument, NewOrganizationDocument } from '../organization-document.model';

export type PartialUpdateOrganizationDocument = Partial<IOrganizationDocument> & Pick<IOrganizationDocument, 'id'>;

export type EntityResponseType = HttpResponse<IOrganizationDocument>;
export type EntityArrayResponseType = HttpResponse<IOrganizationDocument[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationDocumentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/organization-documents');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(organizationDocument: NewOrganizationDocument): Observable<EntityResponseType> {
    return this.http.post<IOrganizationDocument>(this.resourceUrl, organizationDocument, { observe: 'response' });
  }

  update(organizationDocument: IOrganizationDocument): Observable<EntityResponseType> {
    return this.http.put<IOrganizationDocument>(
      `${this.resourceUrl}/${this.getOrganizationDocumentIdentifier(organizationDocument)}`,
      organizationDocument,
      { observe: 'response' }
    );
  }

  partialUpdate(organizationDocument: PartialUpdateOrganizationDocument): Observable<EntityResponseType> {
    return this.http.patch<IOrganizationDocument>(
      `${this.resourceUrl}/${this.getOrganizationDocumentIdentifier(organizationDocument)}`,
      organizationDocument,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IOrganizationDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganizationDocument[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrganizationDocumentIdentifier(organizationDocument: Pick<IOrganizationDocument, 'id'>): string {
    return organizationDocument.id;
  }

  compareOrganizationDocument(o1: Pick<IOrganizationDocument, 'id'> | null, o2: Pick<IOrganizationDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrganizationDocumentIdentifier(o1) === this.getOrganizationDocumentIdentifier(o2) : o1 === o2;
  }

  addOrganizationDocumentToCollectionIfMissing<Type extends Pick<IOrganizationDocument, 'id'>>(
    organizationDocumentCollection: Type[],
    ...organizationDocumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const organizationDocuments: Type[] = organizationDocumentsToCheck.filter(isPresent);
    if (organizationDocuments.length > 0) {
      const organizationDocumentCollectionIdentifiers = organizationDocumentCollection.map(
        organizationDocumentItem => this.getOrganizationDocumentIdentifier(organizationDocumentItem)!
      );
      const organizationDocumentsToAdd = organizationDocuments.filter(organizationDocumentItem => {
        const organizationDocumentIdentifier = this.getOrganizationDocumentIdentifier(organizationDocumentItem);
        if (organizationDocumentCollectionIdentifiers.includes(organizationDocumentIdentifier)) {
          return false;
        }
        organizationDocumentCollectionIdentifiers.push(organizationDocumentIdentifier);
        return true;
      });
      return [...organizationDocumentsToAdd, ...organizationDocumentCollection];
    }
    return organizationDocumentCollection;
  }
}
