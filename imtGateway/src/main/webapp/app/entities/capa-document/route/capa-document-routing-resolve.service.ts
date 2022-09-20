import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICapaDocument } from '../capa-document.model';
import { CapaDocumentService } from '../service/capa-document.service';

@Injectable({ providedIn: 'root' })
export class CapaDocumentRoutingResolveService implements Resolve<ICapaDocument | null> {
  constructor(protected service: CapaDocumentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICapaDocument | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((capaDocument: HttpResponse<ICapaDocument>) => {
          if (capaDocument.body) {
            return of(capaDocument.body);
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
