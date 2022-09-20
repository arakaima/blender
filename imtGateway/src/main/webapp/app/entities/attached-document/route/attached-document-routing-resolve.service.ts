import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAttachedDocument } from '../attached-document.model';
import { AttachedDocumentService } from '../service/attached-document.service';

@Injectable({ providedIn: 'root' })
export class AttachedDocumentRoutingResolveService implements Resolve<IAttachedDocument | null> {
  constructor(protected service: AttachedDocumentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttachedDocument | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((attachedDocument: HttpResponse<IAttachedDocument>) => {
          if (attachedDocument.body) {
            return of(attachedDocument.body);
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
