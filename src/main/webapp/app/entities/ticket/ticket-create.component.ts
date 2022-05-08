import { Component, ElementRef, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDataUtils, JhiEventManager } from 'ng-jhipster';

import { ITicket, TicketCreate } from 'app/shared/model/ticket.model';
import { TicketService } from './ticket.service';
import { ITicketType } from 'app/shared/model/ticket-type.model';
import { TicketTypeService } from 'app/entities/ticket-type/ticket-type.service';
import { IPromoCode } from 'app/shared/model/promo-code.model';
import { PromoCodeService } from 'app/entities/promo-code/promo-code.service';
import { Guest, IGuest } from '../../shared/model/guest.model';
import { GuestService } from '../guest/guest.service';
import { IPromotor } from '../../shared/model/promotor.model';
import { PromotorService } from '../promotor/promotor.service';
import FormUtils from '../../shared/form/form-util';

type SelectableEntity = ITicketType | IPromoCode | IGuest;

@Component({
  selector: 'jhi-ticket-create',
  templateUrl: './ticket-create.component.html',
})
export class TicketCreateComponent implements OnInit {
  SOURCE_ARTIST = 'artist';
  SOURCE_PROMOTOR = 'promotor';

  GUEST_TYPE_NEW = 'register';
  GUEST_TYPE_EXISTING = 'existing';

  isSaving = false;

  newGuest!: IGuest;
  guests: IGuest[] = [];
  selectedGuestType: string = this.GUEST_TYPE_NEW;

  ticketTypes: ITicketType[] = [];

  promotors: IPromotor[] = [];
  promoCodes: IPromoCode[] = [];
  promotorCodes: IPromoCode[] = [];

  selectedSource: String = this.SOURCE_PROMOTOR;

  editForm = this.fb.group({
    guestId: [null],

    newGuestName: [],
    newGuestLastName: [],
    newGuestEmail: [],

    ticketTypeId: [null, Validators.required],

    selectedSource: [this.SOURCE_ARTIST, Validators.required],
    selectedGuestType: [this.GUEST_TYPE_NEW, [Validators.required]],

    promotorId: [],
    promoCodeId: [],

    orderId: [null, [Validators.required]],
    ticketPrice: [null, [Validators.required]],
    ticketDiscount: [0, [Validators.required]],
    artistName: [],
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
  ) {}

  ngOnInit(): void {
    this.loadTicketTypes();
    this.loadPromotors();
    this.loadGuests();
    this.loadPromoCodes();

    this.changedSelectedSource();
    this.onPromotorChanged();
    this.onGuestTypeChanged();
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;

    this.createFromForm().then(ticketCreate => {
      console.info('Creating new ticket');
      this.subscribeToSaveResponse(this.ticketService.create(ticketCreate));
      this.isSaving = false;
    });
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  guestDetails(g: IGuest): string {
    return `#${g.id} ${g.name} ${g.lastName} ${g.email}`;
  }

  onPromotorChanged(): void {
    const promotorId = this.editForm.get(['promotorId'])!.value;
    let promoCodesFromPromotor: IPromoCode[] = [];

    if (promotorId) {
      promoCodesFromPromotor = this.promoCodes.filter(p => p.promotorId === promotorId);
    }

    this.promotorCodes = promoCodesFromPromotor;
  }

  onGuestTypeChanged(): void {
    this.selectedGuestType = this.editForm.get(['selectedGuestType'])!.value;
    console.info('guest type:' + this.selectedGuestType);

    if (this.selectedGuestType === this.GUEST_TYPE_EXISTING) {
      this.editForm.controls.guestId.setValidators([Validators.required]);
      this.changeNewGuestFieldsValidators(null);
    }

    if (this.selectedGuestType === this.GUEST_TYPE_NEW) {
      this.editForm.controls.guestId.clearValidators();
      this.changeNewGuestFieldsValidators([Validators.required]);
    }

    this.editForm.controls.guestId.updateValueAndValidity();
  }

  changedSelectedSource(): void {
    this.selectedSource = this.editForm.get(['selectedSource'])!.value;

    if (this.selectedSource === this.SOURCE_PROMOTOR) {
      this.editForm.controls.promotorId.setValidators([Validators.required]);
      this.editForm.controls.promoCodeId.setValidators([Validators.required]);

      this.editForm.controls.artistName.clearValidators();
    }

    if (this.selectedSource === this.SOURCE_ARTIST) {
      this.editForm.controls.artistName.setValidators([Validators.required]);
      this.editForm.controls.promotorId.clearValidators();
      this.editForm.controls.promoCodeId.clearValidators();
    }

    this.editForm.controls.promotorId.updateValueAndValidity();
    this.editForm.controls.promoCodeId.updateValueAndValidity();
    this.editForm.controls.artistName.updateValueAndValidity();
  }

  getFormValidationErrors(): object[] {
    const formErrors: object[] = [];

    Object.keys(this.editForm.controls).forEach(key => {
      const controlErrors = this.editForm.get(key)?.errors;
      if (controlErrors != null) {
        Object.keys(controlErrors).forEach(keyError => {
          formErrors.push({
            field: key,
            errorType: keyError,
            value: controlErrors[keyError],
          });
        });
      }
    });

    return formErrors;
  }

  fieldInvalid(fieldName: string): boolean {
    return FormUtils.fieldInvalid(this.editForm, fieldName);
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
      .query({ filter: 'ticket-is-null' })
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
    this.promotorsService
      .query()
      .pipe(
        map((res: HttpResponse<IPromotor[]>) => {
          return res.body || [];
        })
      )
      .subscribe((resBody: IPromotor[]) => {
        this.promotors = resBody;
      });
  }

  private createFromForm(): Promise<TicketCreate> {
    return this.useGuest().then(ticketGuestId => {
      return {
        ...new TicketCreate(),
        guestId: ticketGuestId,
        ticketTypeId: this.editForm.get(['ticketTypeId'])!.value,
        promoCodeId: this.editForm.get(['promoCodeId'])!.value,
        promotorId: this.editForm.get(['promotorId'])!.value,
        orderId: this.editForm.get(['orderId'])!.value,
        ticketPrice: this.editForm.get(['ticketPrice'])!.value,
        ticketDiscount: this.editForm.get(['ticketDiscount'])!.value || 0,
        artistName: this.editForm.get(['artistName'])!.value,
      };
    });
  }

  private useGuest(): Promise<number> {
    const newGuest = {
      ...new Guest(),
      name: this.editForm.get(['newGuestName'])!.value,
      lastName: this.editForm.get(['newGuestLastName'])!.value,
      email: this.editForm.get(['newGuestEmail'])!.value,
    };
    const selectedGuestId = this.editForm.get(['guestId'])!.value;

    if (this.selectedGuestType === this.GUEST_TYPE_EXISTING) {
      return Promise.resolve(selectedGuestId);
    } else {
      return this.guestService
        .findOrCreate(newGuest)
        .toPromise()
        .then(res => res.body)
        .then(g => (g?.id ? g.id : selectedGuestId));
    }
  }

  private changeNewGuestFieldsValidators(newGuestFieldsValidators: ValidatorFn | ValidatorFn[] | null): void {
    if (newGuestFieldsValidators === null || newGuestFieldsValidators.length === 0) {
      this.editForm.controls.newGuestName.clearValidators();
      this.editForm.controls.newGuestLastName.clearValidators();
      this.editForm.controls.newGuestEmail.clearValidators();
    } else {
      this.editForm.controls.newGuestName.setValidators(newGuestFieldsValidators);
      this.editForm.controls.newGuestLastName.setValidators(newGuestFieldsValidators);
      this.editForm.controls.newGuestEmail.setValidators(newGuestFieldsValidators);
    }

    this.editForm.controls.newGuestName.updateValueAndValidity();
    this.editForm.controls.newGuestLastName.updateValueAndValidity();
    this.editForm.controls.newGuestEmail.updateValueAndValidity();

    this.editForm.updateValueAndValidity();
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
