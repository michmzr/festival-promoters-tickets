import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPromoCode } from 'app/shared/model/promo-code.model';
import { PromoCodeService } from './promo-code.service';
import { PromoCodeDeleteDialogComponent } from './promo-code-delete-dialog.component';

@Component({
  selector: 'jhi-promo-code',
  templateUrl: './promo-code.component.html',
})
export class PromoCodeComponent implements OnInit, OnDestroy {
  promoCodes?: IPromoCode[];
  eventSubscriber?: Subscription;

  constructor(protected promoCodeService: PromoCodeService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.promoCodeService.query().subscribe((res: HttpResponse<IPromoCode[]>) => (this.promoCodes = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPromoCodes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPromoCode): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPromoCodes(): void {
    this.eventSubscriber = this.eventManager.subscribe('promoCodeListModification', () => this.loadAll());
  }

  delete(promoCode: IPromoCode): void {
    const modalRef = this.modalService.open(PromoCodeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.promoCode = promoCode;
  }
}
