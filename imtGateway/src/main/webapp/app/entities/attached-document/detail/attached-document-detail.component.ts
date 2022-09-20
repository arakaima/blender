import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttachedDocument } from '../attached-document.model';

@Component({
  selector: 'jhi-attached-document-detail',
  templateUrl: './attached-document-detail.component.html',
})
export class AttachedDocumentDetailComponent implements OnInit {
  attachedDocument: IAttachedDocument | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attachedDocument }) => {
      this.attachedDocument = attachedDocument;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
