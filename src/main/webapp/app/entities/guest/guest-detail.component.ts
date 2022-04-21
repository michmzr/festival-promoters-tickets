import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGuest } from 'app/shared/model/guest.model';
import { ITicket, Ticket } from '../../shared/model/ticket.model';
import { TicketService } from '../ticket/ticket.service';
import { HttpResponse } from '@angular/common/http';
import { JhiDataUtils } from 'ng-jhipster';

@Component({
  selector: 'jhi-guest-detail',
  templateUrl: './guest-detail.component.html',
})
export class GuestDetailComponent implements OnInit {
  guest: IGuest | null = null;
  tickets: ITicket[] = [];

  constructor(protected ticketService: TicketService, protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ guest }) => (this.guest = guest));

    this.loadData();
  }

  loadData(): void {
    this.ticketService.query({ 'guestId.equals': this.guest?.id }).subscribe((res: HttpResponse<ITicket[]>) => {
      return (this.tickets = res.body || []);
    });
  }

  downloadFile(ticket: Ticket): void {
    const fileName = 'bilet_' + ticket.uuid + '.pdf';
    return this.dataUtils.downloadFile(
      ticket.ticketFileContentType ? ticket.ticketFileContentType : 'application/pdf',
      ticket.ticketFile,
      fileName
    );
  }

  previousState(): void {
    window.history.back();
  }
}
