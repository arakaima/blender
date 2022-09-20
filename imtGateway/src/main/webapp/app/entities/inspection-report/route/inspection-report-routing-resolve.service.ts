import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInspectionReport } from '../inspection-report.model';
import { InspectionReportService } from '../service/inspection-report.service';

@Injectable({ providedIn: 'root' })
export class InspectionReportRoutingResolveService implements Resolve<IInspectionReport | null> {
  constructor(protected service: InspectionReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInspectionReport | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((inspectionReport: HttpResponse<IInspectionReport>) => {
          if (inspectionReport.body) {
            return of(inspectionReport.body);
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
