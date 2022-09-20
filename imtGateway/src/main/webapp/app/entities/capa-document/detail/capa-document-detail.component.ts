import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICapaDocument } from '../capa-document.model';

@Component({
  selector: 'jhi-capa-document-detail',
  templateUrl: './capa-document-detail.component.html',
})
export class CapaDocumentDetailComponent implements OnInit {
  capaDocument: ICapaDocument | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ capaDocument }) => {
      this.capaDocument = capaDocument;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
