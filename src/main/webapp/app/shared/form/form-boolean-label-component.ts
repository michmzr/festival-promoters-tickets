import { Component, Input, OnDestroy, OnInit } from '@angular/core';

@Component({
  selector: 'form-boolean-label',
  template: ` <span [ngClass]="{ badge: true, 'badge-success': lbValue, 'badge-warning': !lbValue }">{{ lbValue }}</span> `,
})
export class FormBooleanLabelComponent implements OnInit, OnDestroy {
  @Input()
  lbValue: boolean | undefined;

  ngOnDestroy(): void {}

  ngOnInit(): void {
    console.info(`value: ${this.lbValue}`);
  }
}
