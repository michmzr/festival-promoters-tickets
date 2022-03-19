import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IOrderImportResult } from '../../../shared/model/import-order.model';

@Component({
  selector: 'jhi-import-order-result',
  templateUrl: './import-order-result.component.html',
})
export class ImportOrderResultComponent implements OnInit {
  @Input()
  importResult!: IOrderImportResult;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {}

  cardCSSClass(): string {
    if (this.importResult.processed) return 'border-success';
    else return 'border-danger';
  }

  previousState(): void {
    window.history.back();
  }
}
