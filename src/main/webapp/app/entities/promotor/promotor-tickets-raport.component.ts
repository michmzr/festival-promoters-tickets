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
import { KeyValue } from '@angular/common';

@Component({
  selector: 'jhi-promotor-tickets-raport',
  templateUrl: './promotor-tickets-raport.component.html',
})
export class PromotorTicketsRaportComponent {
  promotor: IPromotor | null = null;
  promoCodes: Map<number, IPromoCode> = new Map<number, IPromoCode>();
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

  totalPriceTickets(): number {
    return this.tickets.reduce((accumulator, obj) => {
      return accumulator + (obj.ticketPrice ? obj.ticketPrice : 0);
    }, 0);
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

  totalDiscountsSum(): number {
    return this.tickets.reduce((accumulator, obj) => {
      return accumulator + (obj.ticketDiscount ? obj.ticketDiscount : 0);
    }, 0);
  }

  orderByCodeAsc = (a: KeyValue<number, IPromoCode>, b: KeyValue<number, IPromoCode>): number => {
    return a.value.code && b.value.code ? a.value.code.localeCompare(b.value.code) : 0;
  };

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
    if (this.promotor && this.promotor.promoCodes) {
      const promoCodesProcess: Map<number, IPromoCode> = new Map<number, IPromoCode>();

      this.promotor.promoCodes.forEach(promo => {
        promoCodesProcess.set(promo.id as number, promo);
      });

      this.promoCodes = promoCodesProcess;
    }
  }

  private loadTicketTypes(): void {
    this.ticketTypeService.query().subscribe((res: HttpResponse<ITicketType[]>) => {
      if (res.body)
        res.body.forEach(v => {
          this.ticketTypes[v.id as number] = v;
        });
    });
  }
}
