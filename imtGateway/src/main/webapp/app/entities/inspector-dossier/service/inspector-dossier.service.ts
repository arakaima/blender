import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInspectorDossier, NewInspectorDossier } from '../inspector-dossier.model';

export type PartialUpdateInspectorDossier = Partial<IInspectorDossier> & Pick<IInspectorDossier, 'id'>;

export type EntityResponseType = HttpResponse<IInspectorDossier>;
export type EntityArrayResponseType = HttpResponse<IInspectorDossier[]>;

@Injectable({ providedIn: 'root' })
export class InspectorDossierService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inspector-dossiers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inspectorDossier: NewInspectorDossier): Observable<EntityResponseType> {
    return this.http.post<IInspectorDossier>(this.resourceUrl, inspectorDossier, { observe: 'response' });
  }

  update(inspectorDossier: IInspectorDossier): Observable<EntityResponseType> {
    return this.http.put<IInspectorDossier>(
      `${this.resourceUrl}/${this.getInspectorDossierIdentifier(inspectorDossier)}`,
      inspectorDossier,
      { observe: 'response' }
    );
  }

  partialUpdate(inspectorDossier: PartialUpdateInspectorDossier): Observable<EntityResponseType> {
    return this.http.patch<IInspectorDossier>(
      `${this.resourceUrl}/${this.getInspectorDossierIdentifier(inspectorDossier)}`,
      inspectorDossier,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IInspectorDossier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInspectorDossier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInspectorDossierIdentifier(inspectorDossier: Pick<IInspectorDossier, 'id'>): string {
    return inspectorDossier.id;
  }

  compareInspectorDossier(o1: Pick<IInspectorDossier, 'id'> | null, o2: Pick<IInspectorDossier, 'id'> | null): boolean {
    return o1 && o2 ? this.getInspectorDossierIdentifier(o1) === this.getInspectorDossierIdentifier(o2) : o1 === o2;
  }

  addInspectorDossierToCollectionIfMissing<Type extends Pick<IInspectorDossier, 'id'>>(
    inspectorDossierCollection: Type[],
    ...inspectorDossiersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inspectorDossiers: Type[] = inspectorDossiersToCheck.filter(isPresent);
    if (inspectorDossiers.length > 0) {
      const inspectorDossierCollectionIdentifiers = inspectorDossierCollection.map(
        inspectorDossierItem => this.getInspectorDossierIdentifier(inspectorDossierItem)!
      );
      const inspectorDossiersToAdd = inspectorDossiers.filter(inspectorDossierItem => {
        const inspectorDossierIdentifier = this.getInspectorDossierIdentifier(inspectorDossierItem);
        if (inspectorDossierCollectionIdentifiers.includes(inspectorDossierIdentifier)) {
          return false;
        }
        inspectorDossierCollectionIdentifiers.push(inspectorDossierIdentifier);
        return true;
      });
      return [...inspectorDossiersToAdd, ...inspectorDossierCollection];
    }
    return inspectorDossierCollection;
  }
}
