import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInspectionReport, NewInspectionReport } from '../inspection-report.model';

export type PartialUpdateInspectionReport = Partial<IInspectionReport> & Pick<IInspectionReport, 'id'>;

export type EntityResponseType = HttpResponse<IInspectionReport>;
export type EntityArrayResponseType = HttpResponse<IInspectionReport[]>;

@Injectable({ providedIn: 'root' })
export class InspectionReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inspection-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inspectionReport: NewInspectionReport): Observable<EntityResponseType> {
    return this.http.post<IInspectionReport>(this.resourceUrl, inspectionReport, { observe: 'response' });
  }

  update(inspectionReport: IInspectionReport): Observable<EntityResponseType> {
    return this.http.put<IInspectionReport>(
      `${this.resourceUrl}/${this.getInspectionReportIdentifier(inspectionReport)}`,
      inspectionReport,
      { observe: 'response' }
    );
  }

  partialUpdate(inspectionReport: PartialUpdateInspectionReport): Observable<EntityResponseType> {
    return this.http.patch<IInspectionReport>(
      `${this.resourceUrl}/${this.getInspectionReportIdentifier(inspectionReport)}`,
      inspectionReport,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IInspectionReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInspectionReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInspectionReportIdentifier(inspectionReport: Pick<IInspectionReport, 'id'>): string {
    return inspectionReport.id;
  }

  compareInspectionReport(o1: Pick<IInspectionReport, 'id'> | null, o2: Pick<IInspectionReport, 'id'> | null): boolean {
    return o1 && o2 ? this.getInspectionReportIdentifier(o1) === this.getInspectionReportIdentifier(o2) : o1 === o2;
  }

  addInspectionReportToCollectionIfMissing<Type extends Pick<IInspectionReport, 'id'>>(
    inspectionReportCollection: Type[],
    ...inspectionReportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inspectionReports: Type[] = inspectionReportsToCheck.filter(isPresent);
    if (inspectionReports.length > 0) {
      const inspectionReportCollectionIdentifiers = inspectionReportCollection.map(
        inspectionReportItem => this.getInspectionReportIdentifier(inspectionReportItem)!
      );
      const inspectionReportsToAdd = inspectionReports.filter(inspectionReportItem => {
        const inspectionReportIdentifier = this.getInspectionReportIdentifier(inspectionReportItem);
        if (inspectionReportCollectionIdentifiers.includes(inspectionReportIdentifier)) {
          return false;
        }
        inspectionReportCollectionIdentifiers.push(inspectionReportIdentifier);
        return true;
      });
      return [...inspectionReportsToAdd, ...inspectionReportCollection];
    }
    return inspectionReportCollection;
  }
}
