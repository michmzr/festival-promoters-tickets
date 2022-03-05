import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPromoCode } from 'app/shared/model/promo-code.model';
import { PromoCodeService } from './promo-code.service';

@Component({
  templateUrl: './promo-code-delete-dialog.component.html',
})
export class PromoCodeDeleteDialogComponent {
  promoCode?: IPromoCode;

  constructor(protected promoCodeService: PromoCodeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.promoCodeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('promoCodeListModification');
      this.activeModal.close();
    });
  }
}
