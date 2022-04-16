import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {Subscription} from 'rxjs';
import {JhiDataUtils, JhiEventManager} from 'ng-jhipster';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import {ITicket} from 'app/shared/model/ticket.model';
import {TicketService} from './ticket.service';
import {TicketDeleteDialogComponent} from './ticket-delete-dialog.component';
import {ITicketType} from "../../shared/model/ticket-type.model";
import {map} from "rxjs/operators";
import {TicketTypeService} from "../ticket-type/ticket-type.service";

@Component({
  selector: 'jhi-ticket',
  templateUrl: './ticket.component.html',
})
export class TicketComponent implements OnInit, OnDestroy {
  tickets?: ITicket[];
  ticketTypes: ITicketType[] = [];
  eventSubscriber?: Subscription;

  constructor(
    protected ticketService: TicketService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected ticketTypeService: TicketTypeService,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.ticketService.query().subscribe((res: HttpResponse<ITicket[]>) => (this.tickets = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTickets();
    this.loadTicketTypes();
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

  registerChangeInTickets(): void {
    this.eventSubscriber = this.eventManager.subscribe('ticketListModification', () => this.loadAll());
  }

  delete(ticket: ITicket): void {
    const modalRef = this.modalService.open(TicketDeleteDialogComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.ticket = ticket;
  }


  private loadTicketTypes(): void {
    this.ticketTypeService
      .query({filter: 'ticket-is-null'})
      .pipe(
        map((res: HttpResponse<ITicketType[]>) => {
          return res.body || [];
        })
      )
      .subscribe((resBody: ITicketType[]) => {
        this.ticketTypes = resBody;
      });
  }
}
