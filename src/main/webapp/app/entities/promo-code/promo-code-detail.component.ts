import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPromoCode } from 'app/shared/model/promo-code.model';

@Component({
  selector: 'jhi-promo-code-detail',
  templateUrl: './promo-code-detail.component.html',
})
export class PromoCodeDetailComponent implements OnInit {
  promoCode: IPromoCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promoCode }) => (this.promoCode = promoCode));
  }

  previousState(): void {
    window.history.back();
  }
}
