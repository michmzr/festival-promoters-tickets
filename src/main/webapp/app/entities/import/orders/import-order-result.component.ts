import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IOrderImportResult } from '../../../shared/model/import-moder.model';

@Component({
  selector: 'jhi-import-order-result',
  // templateUrl: './import-order-result.component.html',
})
export class ImportOrderResultComponent implements OnInit {
  importResult?: IOrderImportResult;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {}

  previousState(): void {
    window.history.back();
  }
}
