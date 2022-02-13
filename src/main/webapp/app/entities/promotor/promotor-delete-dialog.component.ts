import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPromotor } from 'app/shared/model/promotor.model';
import { PromotorService } from './promotor.service';

@Component({
  templateUrl: './promotor-delete-dialog.component.html',
})
export class PromotorDeleteDialogComponent {
  promotor?: IPromotor;

  constructor(protected promotorService: PromotorService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.promotorService.delete(id).subscribe(() => {
      this.eventManager.broadcast('promotorListModification');
      this.activeModal.close();
    });
  }
}
