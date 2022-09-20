import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInspector } from '../inspector.model';
import { InspectorService } from '../service/inspector.service';

@Injectable({ providedIn: 'root' })
export class InspectorRoutingResolveService implements Resolve<IInspector | null> {
  constructor(protected service: InspectorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInspector | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((inspector: HttpResponse<IInspector>) => {
          if (inspector.body) {
            return of(inspector.body);
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
