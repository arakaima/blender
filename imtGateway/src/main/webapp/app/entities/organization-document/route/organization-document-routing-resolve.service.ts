import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrganizationDocument } from '../organization-document.model';
import { OrganizationDocumentService } from '../service/organization-document.service';

@Injectable({ providedIn: 'root' })
export class OrganizationDocumentRoutingResolveService implements Resolve<IOrganizationDocument | null> {
  constructor(protected service: OrganizationDocumentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrganizationDocument | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((organizationDocument: HttpResponse<IOrganizationDocument>) => {
          if (organizationDocument.body) {
            return of(organizationDocument.body);
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
