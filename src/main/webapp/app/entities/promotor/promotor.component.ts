import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPromotor } from 'app/shared/model/promotor.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PromotorService } from './promotor.service';
import { PromotorDeleteDialogComponent } from './promotor-delete-dialog.component';

@Component({
  selector: 'jhi-promotor',
  templateUrl: './promotor.component.html',
})
export class PromotorComponent implements OnInit, OnDestroy {
  promotors: IPromotor[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected promotorService: PromotorService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.promotors = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.promotorService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IPromotor[]>) => this.paginatePromotors(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.promotors = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPromotors();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPromotor): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPromotors(): void {
    this.eventSubscriber = this.eventManager.subscribe('promotorListModification', () => this.reset());
  }

  delete(promotor: IPromotor): void {
    const modalRef = this.modalService.open(PromotorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.promotor = promotor;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePromotors(data: IPromotor[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.promotors.push(data[i]);
      }
    }
  }
}
