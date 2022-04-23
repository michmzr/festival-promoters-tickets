import {Component, ElementRef, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {JhiDataUtils, JhiEventManager} from 'ng-jhipster';

import {ITicket, TicketCreate} from 'app/shared/model/ticket.model';
import {TicketService} from './ticket.service';
import {ITicketType} from 'app/shared/model/ticket-type.model';
import {TicketTypeService} from 'app/entities/ticket-type/ticket-type.service';
import {IPromoCode} from 'app/shared/model/promo-code.model';
import {PromoCodeService} from 'app/entities/promo-code/promo-code.service';
import {IGuest} from "../../shared/model/guest.model";
import {GuestService} from "../guest/guest.service";
import {IPromotor} from "../../shared/model/promotor.model";
import {PromotorService} from "../promotor/promotor.service";

type SelectableEntity = ITicketType | IPromoCode;

@Component({
  selector: 'jhi-ticket-create',
  templateUrl: './ticket-create.component.html',
})
export class TicketCreateComponent implements OnInit {
  isSaving = false;

  promoCodes: IPromoCode[] = [];
  guests: IGuest[] = [];
  ticketTypes: ITicketType[] = [];
  promotors: IPromotor[] = [];

  editForm = this.fb.group({
    guestId: [null, [Validators.required]],
    ticketTypeId: [null, [Validators.required]],
    promoCodeId: [],
    promotorId: [],
    orderId: [],
    ticketPrice: []
  });


  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected ticketService: TicketService,
    protected guestService: GuestService,
    protected ticketTypeService: TicketTypeService,
    protected promotorsService: PromotorService,
    protected promoCodeService: PromoCodeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.loadTicketTypes();
    this.loadPromotors();
    this.loadGuests();
    this.loadPromoCodes();
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ticket = this.createFromForm();

    this.subscribeToSaveResponse(this.ticketService.create(ticket));
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITicket>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  protected paginateGuests(data: IGuest[] | null): void {
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.guests.push(data[i]);
      }
    }
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

  private loadPromoCodes(): void {
    this.promoCodeService
      .query()
      .pipe(
        map((res: HttpResponse<IPromoCode[]>) => {
          return res.body || [];
        })
      )
      .subscribe((resBody: IPromoCode[]) => {
        this.promoCodes = resBody;
      });
  }

  private loadGuests(): void {
    this.guestService
      .query({
        page: 0,
        size: 99999,
        sort: this.sortGuests(),
      })
      .subscribe((res: HttpResponse<IGuest[]>) => this.paginateGuests(res.body));
  }

  private loadPromotors(): void {
    this.promotorsService.query()
      .pipe(
        map((res: HttpResponse<IPromotor[]>) => {
          return res.body || [];
        })
      )
      .subscribe((resBody: IPromotor[]) => {
        this.promotors = resBody;
      });
  }

  private createFromForm(): TicketCreate {
    return {
      ...new TicketCreate(),
      guestId: this.editForm.get(['guestId'])!.value,
      ticketTypeId: this.editForm.get(['ticketTypeId'])!.value,
      promoCodeId: this.editForm.get(['promoCodeId'])!.value,
      promotorId: this.editForm.get(['promotorId'])!.value,
      orderId: this.editForm.get(['orderId'])!.value,
      ticketPrice: this.editForm.get(['ticketPrice'])!.value,
    };
  }

  private sortGuests(): string[] {
    const predicate = 'id';
    const ascending = 'asc';

    const result = [predicate + ',' + (ascending ? 'asc' : 'desc')];

    if (predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
}
