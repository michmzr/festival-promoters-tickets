import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITicketType } from 'app/shared/model/ticket-type.model';

@Component({
  selector: 'jhi-ticket-type-detail',
  templateUrl: './ticket-type-detail.component.html',
})
export class TicketTypeDetailComponent implements OnInit {
  ticketType: ITicketType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ticketType }) => (this.ticketType = ticketType));
  }

  previousState(): void {
    window.history.back();
  }
}
