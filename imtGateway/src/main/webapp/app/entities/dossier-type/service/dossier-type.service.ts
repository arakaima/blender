import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDossierType, NewDossierType } from '../dossier-type.model';

export type PartialUpdateDossierType = Partial<IDossierType> & Pick<IDossierType, 'id'>;

export type EntityResponseType = HttpResponse<IDossierType>;
export type EntityArrayResponseType = HttpResponse<IDossierType[]>;

@Injectable({ providedIn: 'root' })
export class DossierTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dossier-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dossierType: NewDossierType): Observable<EntityResponseType> {
    return this.http.post<IDossierType>(this.resourceUrl, dossierType, { observe: 'response' });
  }

  update(dossierType: IDossierType): Observable<EntityResponseType> {
    return this.http.put<IDossierType>(`${this.resourceUrl}/${this.getDossierTypeIdentifier(dossierType)}`, dossierType, {
      observe: 'response',
    });
  }

  partialUpdate(dossierType: PartialUpdateDossierType): Observable<EntityResponseType> {
    return this.http.patch<IDossierType>(`${this.resourceUrl}/${this.getDossierTypeIdentifier(dossierType)}`, dossierType, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IDossierType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDossierType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDossierTypeIdentifier(dossierType: Pick<IDossierType, 'id'>): string {
    return dossierType.id;
  }

  compareDossierType(o1: Pick<IDossierType, 'id'> | null, o2: Pick<IDossierType, 'id'> | null): boolean {
    return o1 && o2 ? this.getDossierTypeIdentifier(o1) === this.getDossierTypeIdentifier(o2) : o1 === o2;
  }

  addDossierTypeToCollectionIfMissing<Type extends Pick<IDossierType, 'id'>>(
    dossierTypeCollection: Type[],
    ...dossierTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dossierTypes: Type[] = dossierTypesToCheck.filter(isPresent);
    if (dossierTypes.length > 0) {
      const dossierTypeCollectionIdentifiers = dossierTypeCollection.map(
        dossierTypeItem => this.getDossierTypeIdentifier(dossierTypeItem)!
      );
      const dossierTypesToAdd = dossierTypes.filter(dossierTypeItem => {
        const dossierTypeIdentifier = this.getDossierTypeIdentifier(dossierTypeItem);
        if (dossierTypeCollectionIdentifiers.includes(dossierTypeIdentifier)) {
          return false;
        }
        dossierTypeCollectionIdentifiers.push(dossierTypeIdentifier);
        return true;
      });
      return [...dossierTypesToAdd, ...dossierTypeCollection];
    }
    return dossierTypeCollection;
  }
}
