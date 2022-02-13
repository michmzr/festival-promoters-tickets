import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPromotor } from 'app/shared/model/promotor.model';

@Component({
  selector: 'jhi-promotor-detail',
  templateUrl: './promotor-detail.component.html',
})
export class PromotorDetailComponent implements OnInit {
  promotor: IPromotor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promotor }) => (this.promotor = promotor));
  }

  previousState(): void {
    window.history.back();
  }
}
