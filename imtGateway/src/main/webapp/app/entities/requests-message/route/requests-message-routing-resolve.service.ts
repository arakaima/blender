import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRequestsMessage } from '../requests-message.model';
import { RequestsMessageService } from '../service/requests-message.service';

@Injectable({ providedIn: 'root' })
export class RequestsMessageRoutingResolveService implements Resolve<IRequestsMessage | null> {
  constructor(protected service: RequestsMessageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRequestsMessage | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((requestsMessage: HttpResponse<IRequestsMessage>) => {
          if (requestsMessage.body) {
            return of(requestsMessage.body);
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
