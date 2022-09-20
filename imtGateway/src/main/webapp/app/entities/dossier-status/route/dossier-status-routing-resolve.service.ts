import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDossierStatus } from '../dossier-status.model';
import { DossierStatusService } from '../service/dossier-status.service';

@Injectable({ providedIn: 'root' })
export class DossierStatusRoutingResolveService implements Resolve<IDossierStatus | null> {
  constructor(protected service: DossierStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDossierStatus | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dossierStatus: HttpResponse<IDossierStatus>) => {
          if (dossierStatus.body) {
            return of(dossierStatus.body);
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
