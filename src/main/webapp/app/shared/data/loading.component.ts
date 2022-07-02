import { Component, Input } from '@angular/core';

@Component({
  selector: 'table-loading',
  template: ` <div *ngIf="show">
    <div class="d-flex justify-content-center">
      <div class="spinner-border" role="status" style="width: 5rem; height: 5rem;">
        <span class="visually-hidden"></span>
      </div>
    </div>
  </div>`,
})
export class LoadingComponent {
  @Input()
  show: Boolean = false;
}
