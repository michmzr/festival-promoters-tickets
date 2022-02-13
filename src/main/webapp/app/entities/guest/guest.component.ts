import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGuest } from 'app/shared/model/guest.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { GuestService } from './guest.service';
import { GuestDeleteDialogComponent } from './guest-delete-dialog.component';

@Component({
  selector: 'jhi-guest',
  templateUrl: './guest.component.html',
})
export class GuestComponent implements OnInit, OnDestroy {
  guests: IGuest[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected guestService: GuestService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.guests = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.guestService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IGuest[]>) => this.paginateGuests(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.guests = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInGuests();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IGuest): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInGuests(): void {
    this.eventSubscriber = this.eventManager.subscribe('guestListModification', () => this.reset());
  }

  delete(guest: IGuest): void {
    const modalRef = this.modalService.open(GuestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.guest = guest;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateGuests(data: IGuest[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.guests.push(data[i]);
      }
    }
  }
}
