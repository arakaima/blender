import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeficiency } from '../deficiency.model';
import { DeficiencyService } from '../service/deficiency.service';

@Injectable({ providedIn: 'root' })
export class DeficiencyRoutingResolveService implements Resolve<IDeficiency | null> {
  constructor(protected service: DeficiencyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeficiency | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deficiency: HttpResponse<IDeficiency>) => {
          if (deficiency.body) {
            return of(deficiency.body);
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
