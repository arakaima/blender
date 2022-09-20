import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeficiency, NewDeficiency } from '../deficiency.model';

export type PartialUpdateDeficiency = Partial<IDeficiency> & Pick<IDeficiency, 'id'>;

export type EntityResponseType = HttpResponse<IDeficiency>;
export type EntityArrayResponseType = HttpResponse<IDeficiency[]>;

@Injectable({ providedIn: 'root' })
export class DeficiencyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/deficiencies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deficiency: NewDeficiency): Observable<EntityResponseType> {
    return this.http.post<IDeficiency>(this.resourceUrl, deficiency, { observe: 'response' });
  }

  update(deficiency: IDeficiency): Observable<EntityResponseType> {
    return this.http.put<IDeficiency>(`${this.resourceUrl}/${this.getDeficiencyIdentifier(deficiency)}`, deficiency, {
      observe: 'response',
    });
  }

  partialUpdate(deficiency: PartialUpdateDeficiency): Observable<EntityResponseType> {
    return this.http.patch<IDeficiency>(`${this.resourceUrl}/${this.getDeficiencyIdentifier(deficiency)}`, deficiency, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IDeficiency>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeficiency[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDeficiencyIdentifier(deficiency: Pick<IDeficiency, 'id'>): string {
    return deficiency.id;
  }

  compareDeficiency(o1: Pick<IDeficiency, 'id'> | null, o2: Pick<IDeficiency, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeficiencyIdentifier(o1) === this.getDeficiencyIdentifier(o2) : o1 === o2;
  }

  addDeficiencyToCollectionIfMissing<Type extends Pick<IDeficiency, 'id'>>(
    deficiencyCollection: Type[],
    ...deficienciesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const deficiencies: Type[] = deficienciesToCheck.filter(isPresent);
    if (deficiencies.length > 0) {
      const deficiencyCollectionIdentifiers = deficiencyCollection.map(deficiencyItem => this.getDeficiencyIdentifier(deficiencyItem)!);
      const deficienciesToAdd = deficiencies.filter(deficiencyItem => {
        const deficiencyIdentifier = this.getDeficiencyIdentifier(deficiencyItem);
        if (deficiencyCollectionIdentifiers.includes(deficiencyIdentifier)) {
          return false;
        }
        deficiencyCollectionIdentifiers.push(deficiencyIdentifier);
        return true;
      });
      return [...deficienciesToAdd, ...deficiencyCollection];
    }
    return deficiencyCollection;
  }
}
