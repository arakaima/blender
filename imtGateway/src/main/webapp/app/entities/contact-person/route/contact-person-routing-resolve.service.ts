import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactPerson } from '../contact-person.model';
import { ContactPersonService } from '../service/contact-person.service';

@Injectable({ providedIn: 'root' })
export class ContactPersonRoutingResolveService implements Resolve<IContactPerson | null> {
  constructor(protected service: ContactPersonService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactPerson | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactPerson: HttpResponse<IContactPerson>) => {
          if (contactPerson.body) {
            return of(contactPerson.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
