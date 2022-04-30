import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { ITicket } from 'app/shared/model/ticket.model';
import { TicketService } from './ticket.service';

@Component({
  templateUrl: './ticket-disable-dialog.component.html',
})
export class TicketDisableDialogComponent {
  ticket?: ITicket;

  constructor(
    protected ticketService: TicketService,
    public activeModal: NgbActiveModal,
    protected alertService: JhiAlertService,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDisable(id: number): void {
    this.ticketService.disable(id).subscribe(() => {
      this.eventManager.broadcast('ticketListModification');
      this.alertService.success(`Ticket #${id} is disabled.`);
      this.activeModal.close();
    });
  }
}
