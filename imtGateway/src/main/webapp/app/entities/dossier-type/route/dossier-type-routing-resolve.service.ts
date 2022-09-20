import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDossierType } from '../dossier-type.model';
import { DossierTypeService } from '../service/dossier-type.service';

@Injectable({ providedIn: 'root' })
export class DossierTypeRoutingResolveService implements Resolve<IDossierType | null> {
  constructor(protected service: DossierTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDossierType | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dossierType: HttpResponse<IDossierType>) => {
          if (dossierType.body) {
            return of(dossierType.body);
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
