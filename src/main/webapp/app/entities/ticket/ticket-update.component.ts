import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ITicket, Ticket } from 'app/shared/model/ticket.model';
import { TicketService } from './ticket.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ITicketType } from 'app/shared/model/ticket-type.model';
import { TicketTypeService } from 'app/entities/ticket-type/ticket-type.service';
import { IPromoCode } from 'app/shared/model/promo-code.model';
import { PromoCodeService } from 'app/entities/promo-code/promo-code.service';

type SelectableEntity = ITicketType | IPromoCode;

@Component({
  selector: 'jhi-ticket-update',
  templateUrl: './ticket-update.component.html',
})
export class TicketUpdateComponent implements OnInit {
  isSaving = false;
  tickettypes: ITicketType[] = [];
  promocodes: IPromoCode[] = [];

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    ticketUrl: [null, [Validators.required]],
    ticketQR: [null, [Validators.required]],
    ticketQRContentType: [],
    ticketFile: [],
    ticketFileContentType: [],
    enabled: [null, [Validators.required]],
    createdAt: [null, [Validators.required]],
    disabledAt: [],
    ticketTypeId: [null, [Validators.required]],
    promoCodeId: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected ticketService: TicketService,
    protected ticketTypeService: TicketTypeService,
    protected promoCodeService: PromoCodeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ticket }) => {
      if (!ticket.id) {
        const today = moment().startOf('day');
        ticket.createdAt = today;
        ticket.disabledAt = today;
      }

      this.updateForm(ticket);

      this.ticketTypeService
        .query({ filter: 'ticket-is-null' })
        .pipe(
          map((res: HttpResponse<ITicketType[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ITicketType[]) => {
          if (!ticket.ticketTypeId) {
            this.tickettypes = resBody;
          } else {
            this.ticketTypeService
              .find(ticket.ticketTypeId)
              .pipe(
                map((subRes: HttpResponse<ITicketType>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ITicketType[]) => (this.tickettypes = concatRes));
          }
        });

      this.promoCodeService
        .query({ filter: 'ticket-is-null' })
        .pipe(
          map((res: HttpResponse<IPromoCode[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPromoCode[]) => {
          if (!ticket.promoCodeId) {
            this.promocodes = resBody;
          } else {
            this.promoCodeService
              .find(ticket.promoCodeId)
              .pipe(
                map((subRes: HttpResponse<IPromoCode>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPromoCode[]) => (this.promocodes = concatRes));
          }
        });
    });
  }

  updateForm(ticket: ITicket): void {
    this.editForm.patchValue({
      id: ticket.id,
      uuid: ticket.uuid,
      ticketUrl: ticket.ticketUrl,
      ticketQR: ticket.ticketQR,
      ticketQRContentType: ticket.ticketQRContentType,
      ticketFile: ticket.ticketFile,
      ticketFileContentType: ticket.ticketFileContentType,
      enabled: ticket.enabled,
      createdAt: ticket.createdAt ? ticket.createdAt.format(DATE_TIME_FORMAT) : null,
      disabledAt: ticket.disabledAt ? ticket.disabledAt.format(DATE_TIME_FORMAT) : null,
      ticketTypeId: ticket.ticketTypeId,
      promoCodeId: ticket.promoCodeId,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('organicPromoTicketsApp.error', { message: err.message })
      );
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ticket = this.createFromForm();
    if (ticket.id !== undefined) {
      this.subscribeToSaveResponse(this.ticketService.update(ticket));
    } else {
      this.subscribeToSaveResponse(this.ticketService.create(ticket));
    }
  }

  private createFromForm(): ITicket {
    return {
      ...new Ticket(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      ticketUrl: this.editForm.get(['ticketUrl'])!.value,
      ticketQRContentType: this.editForm.get(['ticketQRContentType'])!.value,
      ticketQR: this.editForm.get(['ticketQR'])!.value,
      ticketFileContentType: this.editForm.get(['ticketFileContentType'])!.value,
      ticketFile: this.editForm.get(['ticketFile'])!.value,
      enabled: this.editForm.get(['enabled'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      disabledAt: this.editForm.get(['disabledAt'])!.value ? moment(this.editForm.get(['disabledAt'])!.value, DATE_TIME_FORMAT) : undefined,
      ticketTypeId: this.editForm.get(['ticketTypeId'])!.value,
      promoCodeId: this.editForm.get(['promoCodeId'])!.value,
    };
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
