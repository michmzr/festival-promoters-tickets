import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {Subscription} from 'rxjs';
import {JhiEventManager} from 'ng-jhipster';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import {ITicketType} from 'app/shared/model/ticket-type.model';
import {TicketTypeService} from './ticket-type.service';
import {TicketTypeDeleteDialogComponent} from './ticket-type-delete-dialog.component';

@Component({
  selector: 'jhi-ticket-type',
  templateUrl: './ticket-type.component.html',
})
export class TicketTypeComponent implements OnInit, OnDestroy {
  ticketTypes?: ITicketType[];
  eventSubscriber?: Subscription;

  constructor(
    protected ticketTypeService: TicketTypeService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal) {
  }

  loadAll(): void {
    this.ticketTypeService.query().subscribe((res: HttpResponse<ITicketType[]>) => (this.ticketTypes = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTicketTypes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITicketType): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTicketTypes(): void {
    this.eventSubscriber = this.eventManager.subscribe('ticketTypeListModification', () => this.loadAll());
  }

  delete(ticketType: ITicketType): void {
    const modalRef = this.modalService.open(TicketTypeDeleteDialogComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.ticketType = ticketType;
  }
}
