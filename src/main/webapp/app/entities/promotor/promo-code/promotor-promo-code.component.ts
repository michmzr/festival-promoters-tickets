import { Component, Input, OnInit } from '@angular/core';
import { IPromoCode } from 'app/shared/model/promo-code.model';

@Component({
  selector: 'jhi-promotor-promo-code',
  templateUrl: './promotor-promo-code.component.html',
  styleUrls: ['./promotor-promo-code.component.scss'],
})
export class PromotorPromoCodeComponent implements OnInit {
  @Input() promoCode: IPromoCode | null = null;

  constructor() {}

  ngOnInit(): void {}
}
