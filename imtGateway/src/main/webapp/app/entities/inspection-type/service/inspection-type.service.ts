import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInspectionType, NewInspectionType } from '../inspection-type.model';

export type PartialUpdateInspectionType = Partial<IInspectionType> & Pick<IInspectionType, 'id'>;

export type EntityResponseType = HttpResponse<IInspectionType>;
export type EntityArrayResponseType = HttpResponse<IInspectionType[]>;

@Injectable({ providedIn: 'root' })
export class InspectionTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inspection-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inspectionType: NewInspectionType): Observable<EntityResponseType> {
    return this.http.post<IInspectionType>(this.resourceUrl, inspectionType, { observe: 'response' });
  }

  update(inspectionType: IInspectionType): Observable<EntityResponseType> {
    return this.http.put<IInspectionType>(`${this.resourceUrl}/${this.getInspectionTypeIdentifier(inspectionType)}`, inspectionType, {
      observe: 'response',
    });
  }

  partialUpdate(inspectionType: PartialUpdateInspectionType): Observable<EntityResponseType> {
    return this.http.patch<IInspectionType>(`${this.resourceUrl}/${this.getInspectionTypeIdentifier(inspectionType)}`, inspectionType, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IInspectionType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInspectionType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInspectionTypeIdentifier(inspectionType: Pick<IInspectionType, 'id'>): string {
    return inspectionType.id;
  }

  compareInspectionType(o1: Pick<IInspectionType, 'id'> | null, o2: Pick<IInspectionType, 'id'> | null): boolean {
    return o1 && o2 ? this.getInspectionTypeIdentifier(o1) === this.getInspectionTypeIdentifier(o2) : o1 === o2;
  }

  addInspectionTypeToCollectionIfMissing<Type extends Pick<IInspectionType, 'id'>>(
    inspectionTypeCollection: Type[],
    ...inspectionTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inspectionTypes: Type[] = inspectionTypesToCheck.filter(isPresent);
    if (inspectionTypes.length > 0) {
      const inspectionTypeCollectionIdentifiers = inspectionTypeCollection.map(
        inspectionTypeItem => this.getInspectionTypeIdentifier(inspectionTypeItem)!
      );
      const inspectionTypesToAdd = inspectionTypes.filter(inspectionTypeItem => {
        const inspectionTypeIdentifier = this.getInspectionTypeIdentifier(inspectionTypeItem);
        if (inspectionTypeCollectionIdentifiers.includes(inspectionTypeIdentifier)) {
          return false;
        }
        inspectionTypeCollectionIdentifiers.push(inspectionTypeIdentifier);
        return true;
      });
      return [...inspectionTypesToAdd, ...inspectionTypeCollection];
    }
    return inspectionTypeCollection;
  }
}
