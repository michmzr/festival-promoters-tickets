import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {JhiDataUtils} from 'ng-jhipster';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';
import {ITicket} from 'app/shared/model/ticket.model';
import {ITicketType} from '../../shared/model/ticket-type.model';
import {map} from 'rxjs/operators';
import {HttpResponse} from '@angular/common/http';
import {TicketTypeService} from '../ticket-type/ticket-type.service';
import {IGuest} from "../../shared/model/guest.model";
import {GuestService} from "../guest/guest.service";

@Component({
  selector: 'jhi-ticket-detail',
  templateUrl: './ticket-detail.component.html',
})
export class TicketDetailComponent implements OnInit {
  ticket: ITicket | null = null;
  guest: IGuest | null = null;
  qrFileName: string | null = null;
  qrImagePath: SafeResourceUrl | null = null;
  ticketImagePath: SafeResourceUrl | null = null;
  ticketType?: ITicketType;

  constructor(
    protected ticketTypeService: TicketTypeService,
    protected guestService: GuestService,
    protected dataUtils: JhiDataUtils,
    protected activatedRoute: ActivatedRoute,
    private _sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ticket }) => {
      this.ticket = ticket;

      this.qrFileName = 'ticket_qr_' + ticket.uuid + '.png';

      this.qrImagePath = this._sanitizer.bypassSecurityTrustResourceUrl('data:image/png;base64,' + ticket.ticketQR);

      if (ticket.ticketFile)
        this.ticketImagePath = this._sanitizer.bypassSecurityTrustResourceUrl('data:image/png;base64,' + ticket.ticketFile);
      else this.ticketImagePath = this.qrImagePath;

      this.guestService.find(ticket.guestId)
        .subscribe(resp => (this.guest = resp.body));

      if (ticket.ticketTypeId) {
        this.ticketTypeService
          .find(ticket.ticketTypeId)
          .pipe(
            map((subRes: HttpResponse<ITicketType>) => {
              return subRes.body ? subRes.body : {};
            })
          )
          .subscribe((concatRes: ITicketType) => {
            this.ticketType = concatRes;
          });
      } else {
        this.ticketType = {};
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openQrFile(): void {
    this.dataUtils.openFile(this.ticket?.ticketQRContentType!, this.ticket?.ticketQR!);
  }

  openTicketFile(): void {
    this.dataUtils.openFile(this.ticket?.ticketFileContentType!, this.ticket!.ticketFile);
  }

  downloadFile(contentType = '', base64String: string, fileName: string): void {
    this.dataUtils.downloadFile(contentType, base64String, fileName);
  }

  previousState(): void {
    window.history.back();
  }
}
