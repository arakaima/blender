import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { RequestsFormService, RequestsFormGroup } from './requests-form.service';
import { IRequests } from '../requests.model';
import { RequestsService } from '../service/requests.service';

@Component({
  selector: 'jhi-requests-update',
  templateUrl: './requests-update.component.html',
})
export class RequestsUpdateComponent implements OnInit {
  isSaving = false;
  requests: IRequests | null = null;

  editForm: RequestsFormGroup = this.requestsFormService.createRequestsFormGroup();

  constructor(
    protected requestsService: RequestsService,
    protected requestsFormService: RequestsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ requests }) => {
      this.requests = requests;
      if (requests) {
        this.updateForm(requests);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const requests = this.requestsFormService.getRequests(this.editForm);
    if (requests.id !== null) {
      this.subscribeToSaveResponse(this.requestsService.update(requests));
    } else {
      this.subscribeToSaveResponse(this.requestsService.create(requests));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRequests>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(requests: IRequests): void {
    this.requests = requests;
    this.requestsFormService.resetForm(this.editForm, requests);
  }
}
