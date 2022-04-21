import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiAlert, JhiAlertService, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITicket, Ticket } from 'app/shared/model/ticket.model';
import { TicketService } from './ticket.service';
import { TicketDeleteDialogComponent } from './ticket-delete-dialog.component';
import { ITicketType } from '../../shared/model/ticket-type.model';
import { TicketTypeService } from '../ticket-type/ticket-type.service';

@Component({
  selector: 'jhi-ticket',
  templateUrl: './ticket.component.html',
})
export class TicketComponent implements OnInit, OnDestroy {
  tickets: ITicket[] = [];
  ticketTypes: ITicketType[] = [];
  eventSubscriber?: Subscription;
  alerts: JhiAlert[] = [];

  constructor(
    protected ticketService: TicketService,
    protected ticketTypeService: TicketTypeService,
    protected dataUtils: JhiDataUtils,
    protected alertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.ticketService.query().subscribe((res: HttpResponse<ITicket[]>) => (this.tickets = res.body || []));
    this.ticketTypeService.query().subscribe((res: HttpResponse<ITicketType[]>) => {
      (res.body || []).forEach(v => {
        this.ticketTypes[v.id as number] = v;
      });
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTickets();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  ticketTypeDesc(ticketTypeId: number): string {
    if (ticketTypeId in this.ticketTypes) {
      const ticketType: ITicketType = this.ticketTypes[ticketTypeId];
      return `#${ticketType.id} ${ticketType.name}`;
    } else {
      return `#${ticketTypeId} `;
    }
  }

  regenerateFile(ticket: ITicket): void {
    this.ticketService.regenerateTicketPDF(ticket?.id as number).subscribe((res: HttpResponse<ITicket>) => {
      if (res.body) {
        const newTicket = res.body;
        this.tickets = this.tickets.map(t => (t.id === ticket.id ? newTicket : t));
        this.eventManager.broadcast('ticketListModification');
        this.alertService.success('PDF został wygenerowany ponownie');
      }
    });
  }

  trackId(index: number, item: ITicket): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  downloadFile(ticket: Ticket): void {
    const fileName = 'bilet_' + ticket.uuid + '.pdf';
    return this.dataUtils.downloadFile(
      ticket.ticketFileContentType ? ticket.ticketFileContentType : 'application/pdf',
      ticket.ticketFile,
      fileName
    );
  }

  registerChangeInTickets(): void {
    this.eventSubscriber = this.eventManager.subscribe('ticketListModification', () => this.loadAll());
  }

  delete(ticket: ITicket): void {
    const modalRef = this.modalService.open(TicketDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ticket = ticket;
  }
}
