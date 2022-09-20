import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInspectionType } from '../inspection-type.model';
import { InspectionTypeService } from '../service/inspection-type.service';

@Injectable({ providedIn: 'root' })
export class InspectionTypeRoutingResolveService implements Resolve<IInspectionType | null> {
  constructor(protected service: InspectionTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInspectionType | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((inspectionType: HttpResponse<IInspectionType>) => {
          if (inspectionType.body) {
            return of(inspectionType.body);
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
