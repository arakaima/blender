import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactPerson, NewContactPerson } from '../contact-person.model';

export type PartialUpdateContactPerson = Partial<IContactPerson> & Pick<IContactPerson, 'id'>;

export type EntityResponseType = HttpResponse<IContactPerson>;
export type EntityArrayResponseType = HttpResponse<IContactPerson[]>;

@Injectable({ providedIn: 'root' })
export class ContactPersonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-people');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactPerson: NewContactPerson): Observable<EntityResponseType> {
    return this.http.post<IContactPerson>(this.resourceUrl, contactPerson, { observe: 'response' });
  }

  update(contactPerson: IContactPerson): Observable<EntityResponseType> {
    return this.http.put<IContactPerson>(`${this.resourceUrl}/${this.getContactPersonIdentifier(contactPerson)}`, contactPerson, {
      observe: 'response',
    });
  }

  partialUpdate(contactPerson: PartialUpdateContactPerson): Observable<EntityResponseType> {
    return this.http.patch<IContactPerson>(`${this.resourceUrl}/${this.getContactPersonIdentifier(contactPerson)}`, contactPerson, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IContactPerson>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactPerson[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContactPersonIdentifier(contactPerson: Pick<IContactPerson, 'id'>): string {
    return contactPerson.id;
  }

  compareContactPerson(o1: Pick<IContactPerson, 'id'> | null, o2: Pick<IContactPerson, 'id'> | null): boolean {
    return o1 && o2 ? this.getContactPersonIdentifier(o1) === this.getContactPersonIdentifier(o2) : o1 === o2;
  }

  addContactPersonToCollectionIfMissing<Type extends Pick<IContactPerson, 'id'>>(
    contactPersonCollection: Type[],
    ...contactPeopleToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contactPeople: Type[] = contactPeopleToCheck.filter(isPresent);
    if (contactPeople.length > 0) {
      const contactPersonCollectionIdentifiers = contactPersonCollection.map(
        contactPersonItem => this.getContactPersonIdentifier(contactPersonItem)!
      );
      const contactPeopleToAdd = contactPeople.filter(contactPersonItem => {
        const contactPersonIdentifier = this.getContactPersonIdentifier(contactPersonItem);
        if (contactPersonCollectionIdentifiers.includes(contactPersonIdentifier)) {
          return false;
        }
        contactPersonCollectionIdentifiers.push(contactPersonIdentifier);
        return true;
      });
      return [...contactPeopleToAdd, ...contactPersonCollection];
    }
    return contactPersonCollection;
  }
}
