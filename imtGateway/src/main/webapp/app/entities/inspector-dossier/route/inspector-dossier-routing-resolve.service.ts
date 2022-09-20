import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInspectorDossier } from '../inspector-dossier.model';
import { InspectorDossierService } from '../service/inspector-dossier.service';

@Injectable({ providedIn: 'root' })
export class InspectorDossierRoutingResolveService implements Resolve<IInspectorDossier | null> {
  constructor(protected service: InspectorDossierService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInspectorDossier | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((inspectorDossier: HttpResponse<IInspectorDossier>) => {
          if (inspectorDossier.body) {
            return of(inspectorDossier.body);
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
