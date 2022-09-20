import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { InspectorFormService, InspectorFormGroup } from './inspector-form.service';
import { IInspector } from '../inspector.model';
import { InspectorService } from '../service/inspector.service';

@Component({
  selector: 'jhi-inspector-update',
  templateUrl: './inspector-update.component.html',
})
export class InspectorUpdateComponent implements OnInit {
  isSaving = false;
  inspector: IInspector | null = null;

  editForm: InspectorFormGroup = this.inspectorFormService.createInspectorFormGroup();

  constructor(
    protected inspectorService: InspectorService,
    protected inspectorFormService: InspectorFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspector }) => {
      this.inspector = inspector;
      if (inspector) {
        this.updateForm(inspector);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inspector = this.inspectorFormService.getInspector(this.editForm);
    if (inspector.id !== null) {
      this.subscribeToSaveResponse(this.inspectorService.update(inspector));
    } else {
      this.subscribeToSaveResponse(this.inspectorService.create(inspector));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInspector>>): void {
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

  protected updateForm(inspector: IInspector): void {
    this.inspector = inspector;
    this.inspectorFormService.resetForm(this.editForm, inspector);
  }
}
