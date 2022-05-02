import { Component } from '@angular/core';
import { IPromotor } from '../../shared/model/promotor.model';
import { IPromoCode } from '../../shared/model/promo-code.model';
import { PromotorService } from './promotor.service';
import { PromoCodeService } from '../promo-code/promo-code.service';
import { ActivatedRoute } from '@angular/router';
import { TicketService } from '../ticket/ticket.service';
import { ITicket } from '../../shared/model/ticket.model';
import { HttpResponse } from '@angular/common/http';
import { ITicketType } from '../../shared/model/ticket-type.model';
import { TicketTypeService } from '../ticket-type/ticket-type.service';

@Component({
  selector: 'jhi-promotor-tickets-raport',
  templateUrl: './promotor-tickets-raport.component.html',
})
export class PromotorTicketsRaportComponent {
  promotor: IPromotor | null = null;
  promoCodes: IPromoCode[] = [];
  tickets: ITicket[] = [];
  ticketTypes: ITicketType[] = [];

  constructor(
    protected promotorService: PromotorService,
    protected promoCodeService: PromoCodeService,
    protected ticketService: TicketService,
    protected ticketTypeService: TicketTypeService,
    protected activatedRoute: ActivatedRoute
  ) {
    this.onNgInit();
  }

  onNgInit(): void {
    this.activatedRoute.data.subscribe(({ promotor }) => {
      this.promotor = promotor;
      this.onPromotorLoaded();
    });
  }

  ticketTypeDesc(ticketTypeId: number): string {
    if (ticketTypeId in this.ticketTypes) {
      const ticketType: ITicketType = this.ticketTypes[ticketTypeId];
      return `#${ticketType.id} ${ticketType.name}`;
    } else {
      return `#${ticketTypeId} `;
    }
  }

  previousState(): void {
    window.history.back();
  }

  private onPromotorLoaded(): void {
    if (this.promotor) {
      this.loadPromoCodes();
      this.loadPromotorTickets();
      this.loadTicketTypes();
    }
  }

  private loadPromotorTickets(): void {
    this.ticketService.query({ 'promotorId.equals': this.promotor?.id }).subscribe((res: HttpResponse<ITicket[]>) => {
      if (res.body) {
        this.tickets = res.body.sort((a, b) => (a.id && b.id && a.id < b.id ? -1 : 1));
      }
    });
  }

  private loadPromoCodes(): void {
    if (this.promotor) {
      let promoCodesProcess: IPromoCode[] = [];

      (this.promotor.promoCodes || []).forEach(promo => {
        promoCodesProcess[promo.id as number] = promo;
        promoCodesProcess = this.promoCodes.sort((a: IPromoCode, b: IPromoCode) => (a.code && b.code ? a.code.localeCompare(b.code) : 1));
      });
      this.promoCodes = promoCodesProcess;
    }
  }

  private loadTicketTypes(): void {
    this.ticketTypeService.query().subscribe((res: HttpResponse<ITicketType[]>) => {
      (res.body || []).forEach(v => {
        this.ticketTypes[v.id as number] = v;
      });
    });
  }
}
