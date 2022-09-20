import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICapa, NewCapa } from '../capa.model';

export type PartialUpdateCapa = Partial<ICapa> & Pick<ICapa, 'id'>;

export type EntityResponseType = HttpResponse<ICapa>;
export type EntityArrayResponseType = HttpResponse<ICapa[]>;

@Injectable({ providedIn: 'root' })
export class CapaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/capas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(capa: NewCapa): Observable<EntityResponseType> {
    return this.http.post<ICapa>(this.resourceUrl, capa, { observe: 'response' });
  }

  update(capa: ICapa): Observable<EntityResponseType> {
    return this.http.put<ICapa>(`${this.resourceUrl}/${this.getCapaIdentifier(capa)}`, capa, { observe: 'response' });
  }

  partialUpdate(capa: PartialUpdateCapa): Observable<EntityResponseType> {
    return this.http.patch<ICapa>(`${this.resourceUrl}/${this.getCapaIdentifier(capa)}`, capa, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICapa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICapa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCapaIdentifier(capa: Pick<ICapa, 'id'>): string {
    return capa.id;
  }

  compareCapa(o1: Pick<ICapa, 'id'> | null, o2: Pick<ICapa, 'id'> | null): boolean {
    return o1 && o2 ? this.getCapaIdentifier(o1) === this.getCapaIdentifier(o2) : o1 === o2;
  }

  addCapaToCollectionIfMissing<Type extends Pick<ICapa, 'id'>>(
    capaCollection: Type[],
    ...capasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const capas: Type[] = capasToCheck.filter(isPresent);
    if (capas.length > 0) {
      const capaCollectionIdentifiers = capaCollection.map(capaItem => this.getCapaIdentifier(capaItem)!);
      const capasToAdd = capas.filter(capaItem => {
        const capaIdentifier = this.getCapaIdentifier(capaItem);
        if (capaCollectionIdentifiers.includes(capaIdentifier)) {
          return false;
        }
        capaCollectionIdentifiers.push(capaIdentifier);
        return true;
      });
      return [...capasToAdd, ...capaCollection];
    }
    return capaCollection;
  }
}
