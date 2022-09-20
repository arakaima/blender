import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { RequestsMessageFormService, RequestsMessageFormGroup } from './requests-message-form.service';
import { IRequestsMessage } from '../requests-message.model';
import { RequestsMessageService } from '../service/requests-message.service';

@Component({
  selector: 'jhi-requests-message-update',
  templateUrl: './requests-message-update.component.html',
})
export class RequestsMessageUpdateComponent implements OnInit {
  isSaving = false;
  requestsMessage: IRequestsMessage | null = null;

  editForm: RequestsMessageFormGroup = this.requestsMessageFormService.createRequestsMessageFormGroup();

  constructor(
    protected requestsMessageService: RequestsMessageService,
    protected requestsMessageFormService: RequestsMessageFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ requestsMessage }) => {
      this.requestsMessage = requestsMessage;
      if (requestsMessage) {
        this.updateForm(requestsMessage);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const requestsMessage = this.requestsMessageFormService.getRequestsMessage(this.editForm);
    if (requestsMessage.id !== null) {
      this.subscribeToSaveResponse(this.requestsMessageService.update(requestsMessage));
    } else {
      this.subscribeToSaveResponse(this.requestsMessageService.create(requestsMessage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRequestsMessage>>): void {
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

  protected updateForm(requestsMessage: IRequestsMessage): void {
    this.requestsMessage = requestsMessage;
    this.requestsMessageFormService.resetForm(this.editForm, requestsMessage);
  }
}
