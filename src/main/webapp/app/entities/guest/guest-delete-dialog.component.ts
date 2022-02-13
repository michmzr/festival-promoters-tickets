import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGuest } from 'app/shared/model/guest.model';
import { GuestService } from './guest.service';

@Component({
  templateUrl: './guest-delete-dialog.component.html',
})
export class GuestDeleteDialogComponent {
  guest?: IGuest;

  constructor(protected guestService: GuestService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.guestService.delete(id).subscribe(() => {
      this.eventManager.broadcast('guestListModification');
      this.activeModal.close();
    });
  }
}
