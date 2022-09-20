import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDossierStatus, NewDossierStatus } from '../dossier-status.model';

export type PartialUpdateDossierStatus = Partial<IDossierStatus> & Pick<IDossierStatus, 'id'>;

export type EntityResponseType = HttpResponse<IDossierStatus>;
export type EntityArrayResponseType = HttpResponse<IDossierStatus[]>;

@Injectable({ providedIn: 'root' })
export class DossierStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dossier-statuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dossierStatus: NewDossierStatus): Observable<EntityResponseType> {
    return this.http.post<IDossierStatus>(this.resourceUrl, dossierStatus, { observe: 'response' });
  }

  update(dossierStatus: IDossierStatus): Observable<EntityResponseType> {
    return this.http.put<IDossierStatus>(`${this.resourceUrl}/${this.getDossierStatusIdentifier(dossierStatus)}`, dossierStatus, {
      observe: 'response',
    });
  }

  partialUpdate(dossierStatus: PartialUpdateDossierStatus): Observable<EntityResponseType> {
    return this.http.patch<IDossierStatus>(`${this.resourceUrl}/${this.getDossierStatusIdentifier(dossierStatus)}`, dossierStatus, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IDossierStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDossierStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDossierStatusIdentifier(dossierStatus: Pick<IDossierStatus, 'id'>): string {
    return dossierStatus.id;
  }

  compareDossierStatus(o1: Pick<IDossierStatus, 'id'> | null, o2: Pick<IDossierStatus, 'id'> | null): boolean {
    return o1 && o2 ? this.getDossierStatusIdentifier(o1) === this.getDossierStatusIdentifier(o2) : o1 === o2;
  }

  addDossierStatusToCollectionIfMissing<Type extends Pick<IDossierStatus, 'id'>>(
    dossierStatusCollection: Type[],
    ...dossierStatusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dossierStatuses: Type[] = dossierStatusesToCheck.filter(isPresent);
    if (dossierStatuses.length > 0) {
      const dossierStatusCollectionIdentifiers = dossierStatusCollection.map(
        dossierStatusItem => this.getDossierStatusIdentifier(dossierStatusItem)!
      );
      const dossierStatusesToAdd = dossierStatuses.filter(dossierStatusItem => {
        const dossierStatusIdentifier = this.getDossierStatusIdentifier(dossierStatusItem);
        if (dossierStatusCollectionIdentifiers.includes(dossierStatusIdentifier)) {
          return false;
        }
        dossierStatusCollectionIdentifiers.push(dossierStatusIdentifier);
        return true;
      });
      return [...dossierStatusesToAdd, ...dossierStatusCollection];
    }
    return dossierStatusCollection;
  }
}
