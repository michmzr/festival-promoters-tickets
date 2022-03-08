import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { Guest, IGuest } from 'app/shared/model/guest.model';
import { GuestService } from './guest.service';
import { ITicket } from 'app/shared/model/ticket.model';
import { TicketService } from 'app/entities/ticket/ticket.service';
import { IPromotor } from 'app/shared/model/promotor.model';
import { PromotorService } from 'app/entities/promotor/promotor.service';
import { TicketTypeService } from '../ticket-type/ticket-type.service';
import { ITicketType } from '../../shared/model/ticket-type.model';

type SelectableEntity = ITicket | IPromotor;

@Component({
  selector: 'jhi-guest-update',
  templateUrl: './guest-update.component.html',
})
export class GuestUpdateComponent implements OnInit {
  isSaving = false;
  tickets: ITicket[] = [];
  ticketTypes: ITicketType[] = [];
  promotors: IPromotor[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    email: [null, [Validators.required]],
    phoneNumber: [],
    notes: [null, [Validators.maxLength(500)]],
    enabled: [null, [Validators.required]],
  });

  constructor(
    protected guestService: GuestService,
    protected ticketService: TicketService,
    protected promotorService: PromotorService,
    protected ticketTypeService: TicketTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ guest }) => {
      if (!guest.id) {
        const today = moment().startOf('day');
        guest.createdAt = today;
      }

      this.updateForm(guest);

      this.ticketService
        .query({ filter: 'guest-is-null' })
        .pipe(
          map((res: HttpResponse<ITicket[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ITicket[]) => {
          if (!guest.ticketId) {
            this.tickets = resBody;
          } else {
            this.ticketService
              .find(guest.ticketId)
              .pipe(
                map((subRes: HttpResponse<ITicket>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ITicket[]) => (this.tickets = concatRes));
          }
        });

      this.ticketTypeService
        .query({ filter: 'ticket-is-null' })
        .pipe(
          map((res: HttpResponse<ITicketType[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ITicketType[]) => {
          if (!guest.ticketTypeId) {
            this.ticketTypes = resBody;
          } else {
            this.ticketTypeService
              .find(guest.ticketTypeId)
              .pipe(
                map((subRes: HttpResponse<ITicketType>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ITicketType[]) => (this.ticketTypes = concatRes));
          }
        });

      this.promotorService.query().subscribe((res: HttpResponse<IPromotor[]>) => (this.promotors = res.body || []));
    });
  }

  updateForm(guest: IGuest): void {
    this.editForm.patchValue({
      id: guest.id,
      name: guest.name,
      lastName: guest.lastName,
      email: guest.email,
      phoneNumber: guest.phoneNumber,
      notes: guest.notes,
      enabled: guest.enabled,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const guest = this.createFromForm();

    if (guest.id !== undefined) {
      this.subscribeToSaveResponse(this.guestService.update(guest));
    } else {
      this.subscribeToSaveResponse(this.guestService.create(guest));
    }
  }

  private createFromForm(): IGuest {
    return {
      ...new Guest(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      enabled: this.editForm.get(['enabled'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGuest>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
