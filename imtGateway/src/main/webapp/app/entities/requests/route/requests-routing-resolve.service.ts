import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRequests } from '../requests.model';
import { RequestsService } from '../service/requests.service';

@Injectable({ providedIn: 'root' })
export class RequestsRoutingResolveService implements Resolve<IRequests | null> {
  constructor(protected service: RequestsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRequests | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((requests: HttpResponse<IRequests>) => {
          if (requests.body) {
            return of(requests.body);
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
