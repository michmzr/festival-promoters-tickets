import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITicketType } from 'app/shared/model/ticket-type.model';
import { TicketTypeService } from './ticket-type.service';

@Component({
  templateUrl: './ticket-type-delete-dialog.component.html',
})
export class TicketTypeDeleteDialogComponent {
  ticketType?: ITicketType;

  constructor(
    protected ticketTypeService: TicketTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ticketTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ticketTypeListModification');
      this.activeModal.close();
    });
  }
}
