import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInspection, NewInspection } from '../inspection.model';

export type PartialUpdateInspection = Partial<IInspection> & Pick<IInspection, 'id'>;

export type EntityResponseType = HttpResponse<IInspection>;
export type EntityArrayResponseType = HttpResponse<IInspection[]>;

@Injectable({ providedIn: 'root' })
export class InspectionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inspections');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inspection: NewInspection): Observable<EntityResponseType> {
    return this.http.post<IInspection>(this.resourceUrl, inspection, { observe: 'response' });
  }

  update(inspection: IInspection): Observable<EntityResponseType> {
    return this.http.put<IInspection>(`${this.resourceUrl}/${this.getInspectionIdentifier(inspection)}`, inspection, {
      observe: 'response',
    });
  }

  partialUpdate(inspection: PartialUpdateInspection): Observable<EntityResponseType> {
    return this.http.patch<IInspection>(`${this.resourceUrl}/${this.getInspectionIdentifier(inspection)}`, inspection, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IInspection>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInspection[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInspectionIdentifier(inspection: Pick<IInspection, 'id'>): string {
    return inspection.id;
  }

  compareInspection(o1: Pick<IInspection, 'id'> | null, o2: Pick<IInspection, 'id'> | null): boolean {
    return o1 && o2 ? this.getInspectionIdentifier(o1) === this.getInspectionIdentifier(o2) : o1 === o2;
  }

  addInspectionToCollectionIfMissing<Type extends Pick<IInspection, 'id'>>(
    inspectionCollection: Type[],
    ...inspectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inspections: Type[] = inspectionsToCheck.filter(isPresent);
    if (inspections.length > 0) {
      const inspectionCollectionIdentifiers = inspectionCollection.map(inspectionItem => this.getInspectionIdentifier(inspectionItem)!);
      const inspectionsToAdd = inspections.filter(inspectionItem => {
        const inspectionIdentifier = this.getInspectionIdentifier(inspectionItem);
        if (inspectionCollectionIdentifiers.includes(inspectionIdentifier)) {
          return false;
        }
        inspectionCollectionIdentifiers.push(inspectionIdentifier);
        return true;
      });
      return [...inspectionsToAdd, ...inspectionCollection];
    }
    return inspectionCollection;
  }
}
