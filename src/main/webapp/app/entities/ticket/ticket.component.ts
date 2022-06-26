import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiDataUtils, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITicket, Ticket, TicketListingItem } from 'app/shared/model/ticket.model';
import { TicketService } from './ticket.service';
import { TicketDeleteDialogComponent } from './ticket-delete-dialog.component';
import { ITicketType } from '../../shared/model/ticket-type.model';
import { TicketTypeService } from '../ticket-type/ticket-type.service';
import { TicketDisableDialogComponent } from './ticket-disable-dialog.component';
import { IPromoCode } from '../../shared/model/promo-code.model';
import { PromoCodeService } from '../promo-code/promo-code.service';
import { map } from 'rxjs/operators';
import { ITEMS_PER_PAGE } from '../../shared/constants/pagination.constants';
import { IPromotor } from '../../shared/model/promotor.model';

const EVENT_ON_LIST_MODIFIED = 'ticketListModification';

@Component({
  selector: 'jhi-ticket',
  templateUrl: './ticket.component.html',
})
export class TicketComponent implements OnInit, OnDestroy {
  tickets: TicketListingItem[] = [];
  ticketTypes: ITicketType[] = [];
  promoCodes: Map<number, IPromoCode> = new Map<number, IPromoCode>();
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  isLoading: Boolean = true;

  constructor(
    protected ticketService: TicketService,
    protected ticketTypeService: TicketTypeService,
    protected promoCodeService: PromoCodeService,
    protected dataUtils: JhiDataUtils,
    protected alertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = false;
    this.tickets = [];
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTickets();
  }

  loadAll(): void {
    this.loadTicketTypes();
    this.loadPromoCodes();

    this.loadTickets();
  }

  getTicket(id: number): ITicket {
    return this.ticketService.findSync(id);
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
    const modalRef = this.modalService.open(TicketDisableDialogComponent, {
      size: 'lg',
      backdrop: 'static',
    });
    modalRef.componentInstance.ticket = ticket;

    this.tickets.forEach(t => {
      if (t.id === ticket.id) {
        t.enabled = false;
      }
    });
  }

  enable(ticket: ITicket): void {
    const id = ticket.id;
    if (id) {
      this.ticketService.enable(id).subscribe(() => {
        this.eventManager.broadcast(EVENT_ON_LIST_MODIFIED);
        this.alertService.success(`Ticket #${id} is enabled again. Can be used by guest.`);

        this.tickets.forEach(t => {
          if (t.id === ticket.id) {
            t.enabled = true;
          }
        });
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

  downloadFile(ticketItem: TicketListingItem): void {
    if (ticketItem.id) {
      this.isLoading = true;

      this.ticketService.find(ticketItem.id).subscribe((ticketRes: HttpResponse<Ticket>) => {
        if (ticketRes.body) {
          const ticket: ITicket = ticketRes.body;
          const fileName = this.ticketService.getPdfFileName(ticket);
          // @ts-ignore
          this.isLoading = false;

          this.dataUtils.downloadFile(
            ticket.ticketFileContentType ? ticket.ticketFileContentType : 'application/pdf',
            ticket.ticketFile,
            fileName
          );
        } else {
          console.error('Not found ticket');
        }
      });
    }
  }

  registerChangeInTickets(): void {
    this.eventSubscriber = this.eventManager.subscribe(EVENT_ON_LIST_MODIFIED, () => {
      console.info('got event - list modified');
      this.reloadTickets();
    });
  }

  delete(ticket: ITicket): void {
    const modalRef = this.modalService.open(TicketDeleteDialogComponent, {
      size: 'lg',
      backdrop: 'static',
    });
    modalRef.componentInstance.ticket = ticket;
  }

  canLoadMore(): boolean {
    return this.page <= this.links['last'];
  }

  private loadTicketTypes(): void {
    this.ticketTypeService.query().subscribe((res: HttpResponse<ITicketType[]>) => {
      (res.body || []).forEach(v => {
        this.ticketTypes[v.id as number] = v;
      });
    });
  }

  private reloadTickets(): void {
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.tickets = [];
    this.loadTickets();
  }

  private loadTickets(): void {
    this.isLoading = true;
    this.ticketService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<TicketListingItem[]>) => this.paginateTickets(res.body, res.headers));
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadTickets();
  }

  protected paginateTickets(data: IPromotor[] | null, headers: HttpHeaders): void {
    if (data != null) {
      const headersLink = headers.get('link');
      this.links = this.parseLinks.parse(headersLink ? headersLink : '');
      if (data) {
        for (let i = 0; i < data.length; i++) {
          this.tickets.push(data[i]);
        }
      }
    }

    this.isLoading = false;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
}
