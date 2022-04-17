import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {Subscription} from 'rxjs';
import {JhiDataUtils, JhiEventManager} from 'ng-jhipster';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import {ITicket, Ticket} from 'app/shared/model/ticket.model';
import {TicketService} from './ticket.service';
import {TicketDeleteDialogComponent} from './ticket-delete-dialog.component';

@Component({
  selector: 'jhi-ticket',
  templateUrl: './ticket.component.html',
})
export class TicketComponent implements OnInit, OnDestroy {
  tickets?: ITicket[];
  eventSubscriber?: Subscription;

  constructor(
    protected ticketService: TicketService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.ticketService.query().subscribe((res: HttpResponse<ITicket[]>) => (this.tickets = res.body || []));
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
    const fileName = "bilet_" + ticket.uuid + ".pdf";
    return this.dataUtils.downloadFile(
      ticket.ticketFileContentType ? ticket.ticketFileContentType : "application/pdf", ticket.ticketFile, fileName)
  }

  registerChangeInTickets(): void {
    this.eventSubscriber = this.eventManager.subscribe('ticketListModification', () => this.loadAll());
  }

  delete(ticket: ITicket): void {
    const modalRef = this.modalService.open(TicketDeleteDialogComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.ticket = ticket;
  }
}
