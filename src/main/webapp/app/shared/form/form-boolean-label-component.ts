import { Component, Input } from '@angular/core';

@Component({
  selector: 'form-boolean-label',
  template: `<span [ngClass]="{ badge: true, 'badge-success': lbValue, 'badge-warning': !lbValue }">{{ lbValue }}</span>`,
})
export class FormBooleanLabelComponent {
  @Input()
  lbValue: boolean | undefined;
}
