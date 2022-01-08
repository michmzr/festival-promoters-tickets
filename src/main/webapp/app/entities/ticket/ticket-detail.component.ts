import { Component, OnInit } from '@angular/core';
import { JhiDataUtils } from 'ng-jhipster';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

import { ITicket } from 'app/shared/model/ticket.model';
import { ActivatedRoute } from '@angular/router';
import { ITicketType } from '../../shared/model/ticket-type.model';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { TicketTypeService } from '../ticket-type/ticket-type.service';

@Component({
  selector: 'jhi-ticket-detail',
  templateUrl: './ticket-detail.component.html',
})
export class TicketDetailComponent implements OnInit {
  ticket: ITicket | null = null;
  qrFileName: string | null = null;
  qrImagePath: SafeResourceUrl | null = null;
  ticketTypes: ITicketType[] = [];

  constructor(
    protected ticketTypeService: TicketTypeService,
    protected dataUtils: JhiDataUtils,
    protected activatedRoute: ActivatedRoute,
    private _sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ticket }) => {
      this.ticket = ticket;
      this.qrFileName = 'ticket_qr_' + ticket.uuid + '.png';
      this.qrImagePath = this._sanitizer.bypassSecurityTrustResourceUrl('data:image/png;base64,' + ticket.ticketQR);

      this.ticketTypeService
        .query({ filter: 'ticket-is-null' })
        .pipe(
          map((res: HttpResponse<ITicketType[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ITicketType[]) => {
          if (!ticket.ticketTypeId) {
            this.ticketTypes = resBody;
          } else {
            this.ticketTypeService
              .find(ticket.ticketTypeId)
              .pipe(
                map((subRes: HttpResponse<ITicketType>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ITicketType[]) => (this.ticketTypes = concatRes));
          }
        });
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  downloadFile(contentType = '', base64String: string, fileName: string): void {
    this.dataUtils.downloadFile(contentType, base64String, fileName);
  }

  previousState(): void {
    window.history.back();
  }
}
