import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITicket, TicketCreate, TicketListingItem } from 'app/shared/model/ticket.model';
import { SyncRequestClient } from 'ts-sync-request/dist';

type EntityResponseType = HttpResponse<ITicket>;
type EntityArrayResponseType = HttpResponse<TicketListingItem[]>;

@Injectable({ providedIn: 'root' })
export class TicketService {
  public resourceUrl = SERVER_API_URL + 'api/tickets';

  constructor(protected http: HttpClient) {}

  create(ticket: TicketCreate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ticket);
    return this.http
      .post<TicketCreate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ticket: ITicket): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ticket);
    return this.http
      .put<ITicket>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITicket>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findSync(id: number): ITicket {
    return new SyncRequestClient().get<ITicket>(`${this.resourceUrl}/${id}`);
  }

  regenerateTicketPDF(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITicket>(`${this.resourceUrl}/${id}/rebuild`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITicket[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  disable(id: number): Observable<Object> {
    return this.http
      .post<Object>(`${this.resourceUrl}/${id}/disable`, {}, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => res.body));
  }

  enable(id: number): Observable<Object> {
    return this.http
      .post<Object>(`${this.resourceUrl}/${id}/enable`, {}, { observe: 'response' })
      .pipe(map((res: HttpResponse<any>) => res.body));
  }

  protected convertDateFromClient(ticket: ITicket): ITicket {
    const copy: ITicket = Object.assign({}, ticket, {
      createdAt: ticket.createdAt && ticket.createdAt.isValid() ? ticket.createdAt.toJSON() : undefined,
      disabledAt: ticket.disabledAt && ticket.disabledAt.isValid() ? ticket.disabledAt.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
      res.body.disabledAt = res.body.disabledAt ? moment(res.body.disabledAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((ticket: ITicket) => {
        ticket.createdAt = ticket.createdAt ? moment(ticket.createdAt) : undefined;
        ticket.disabledAt = ticket.disabledAt ? moment(ticket.disabledAt) : undefined;
      });
    }
    return res;
  }
}
