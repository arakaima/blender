import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExpert } from '../expert.model';
import { ExpertService } from '../service/expert.service';

@Injectable({ providedIn: 'root' })
export class ExpertRoutingResolveService implements Resolve<IExpert | null> {
  constructor(protected service: ExpertService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExpert | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((expert: HttpResponse<IExpert>) => {
          if (expert.body) {
            return of(expert.body);
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
