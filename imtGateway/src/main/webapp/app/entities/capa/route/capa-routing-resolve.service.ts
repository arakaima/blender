import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICapa } from '../capa.model';
import { CapaService } from '../service/capa.service';

@Injectable({ providedIn: 'root' })
export class CapaRoutingResolveService implements Resolve<ICapa | null> {
  constructor(protected service: CapaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICapa | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((capa: HttpResponse<ICapa>) => {
          if (capa.body) {
            return of(capa.body);
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
