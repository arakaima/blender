import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInspector, NewInspector } from '../inspector.model';

export type PartialUpdateInspector = Partial<IInspector> & Pick<IInspector, 'id'>;

export type EntityResponseType = HttpResponse<IInspector>;
export type EntityArrayResponseType = HttpResponse<IInspector[]>;

@Injectable({ providedIn: 'root' })
export class InspectorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inspectors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inspector: NewInspector): Observable<EntityResponseType> {
    return this.http.post<IInspector>(this.resourceUrl, inspector, { observe: 'response' });
  }

  update(inspector: IInspector): Observable<EntityResponseType> {
    return this.http.put<IInspector>(`${this.resourceUrl}/${this.getInspectorIdentifier(inspector)}`, inspector, { observe: 'response' });
  }

  partialUpdate(inspector: PartialUpdateInspector): Observable<EntityResponseType> {
    return this.http.patch<IInspector>(`${this.resourceUrl}/${this.getInspectorIdentifier(inspector)}`, inspector, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IInspector>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInspector[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInspectorIdentifier(inspector: Pick<IInspector, 'id'>): string {
    return inspector.id;
  }

  compareInspector(o1: Pick<IInspector, 'id'> | null, o2: Pick<IInspector, 'id'> | null): boolean {
    return o1 && o2 ? this.getInspectorIdentifier(o1) === this.getInspectorIdentifier(o2) : o1 === o2;
  }

  addInspectorToCollectionIfMissing<Type extends Pick<IInspector, 'id'>>(
    inspectorCollection: Type[],
    ...inspectorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inspectors: Type[] = inspectorsToCheck.filter(isPresent);
    if (inspectors.length > 0) {
      const inspectorCollectionIdentifiers = inspectorCollection.map(inspectorItem => this.getInspectorIdentifier(inspectorItem)!);
      const inspectorsToAdd = inspectors.filter(inspectorItem => {
        const inspectorIdentifier = this.getInspectorIdentifier(inspectorItem);
        if (inspectorCollectionIdentifiers.includes(inspectorIdentifier)) {
          return false;
        }
        inspectorCollectionIdentifiers.push(inspectorIdentifier);
        return true;
      });
      return [...inspectorsToAdd, ...inspectorCollection];
    }
    return inspectorCollection;
  }
}
