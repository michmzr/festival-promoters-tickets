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
import { TicketDisableDialogComponent } from './ticket-disable-dialog.component';
import { IPromoCode } from '../../shared/model/promo-code.model';
import { PromoCodeService } from '../promo-code/promo-code.service';
import { map } from 'rxjs/operators';

@Component({
  selector: 'jhi-ticket',
  templateUrl: './ticket.component.html',
})
export class TicketComponent implements OnInit, OnDestroy {
  tickets: ITicket[] = [];
  ticketTypes: ITicketType[] = [];
  promoCodes: Map<number, IPromoCode> = new Map<number, IPromoCode>();
  eventSubscriber?: Subscription;
  alerts: JhiAlert[] = [];

  constructor(
    protected ticketService: TicketService,
    protected ticketTypeService: TicketTypeService,
    protected promoCodeService: PromoCodeService,
    protected dataUtils: JhiDataUtils,
    protected alertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTickets();
  }

  loadAll(): void {
    this.loadTicketTypes();
    this.loadPromoCodes();

    this.loadTickets();
  }

  promoCodeText(id: number): string {
    if (this.promoCodes.has(id)) {
      const promo = this.promoCodes.get(id);
      return `#${promo?.id} "${promo?.code}"`;
    } else {
      return `#${id}`;
    }
  }

  disable(ticket: ITicket): void {
    const modalRef = this.modalService.open(TicketDisableDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ticket = ticket;
  }

  enable(ticket: ITicket): void {
    const id = ticket.id;
    if (id) {
      this.ticketService.enable(id).subscribe(() => {
        this.eventManager.broadcast('ticketListModification');
        this.alertService.success(`Ticket #${id} is enabled again. Can be used by guest.`);
      });
    }
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

  private loadPromoCodes(): void {
    this.promoCodeService
      .query()
      .pipe(
        map((res: HttpResponse<IPromoCode[]>) => {
          return res.body || [];
        })
      )
      .subscribe((resBody: IPromoCode[]) => {
        const promoMap: Map<number, IPromoCode> = new Map<number, IPromoCode>();
        resBody.forEach(promo => {
          promoMap.set(promo.id as number, promo);
        });
        this.promoCodes = promoMap;
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
    this.loadTickets();
  }

  private loadTicketTypes(): void {
    this.ticketTypeService.query().subscribe((res: HttpResponse<ITicketType[]>) => {
      (res.body || []).forEach(v => {
        this.ticketTypes[v.id as number] = v;
      });
    });
  }

  private loadTickets(): void {
    this.ticketService.query().subscribe((res: HttpResponse<ITicket[]>) => {
      if (res.body) {
        this.tickets = res.body.sort((a, b) => (a.id && b.id && a.id < b.id ? -1 : 1));
      } else {
        this.tickets = [];
      }
    });
  }
}
