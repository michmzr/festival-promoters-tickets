import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITicketType } from 'app/shared/model/ticket-type.model';

type EntityResponseType = HttpResponse<ITicketType>;
type EntityArrayResponseType = HttpResponse<ITicketType[]>;

@Injectable({ providedIn: 'root' })
export class TicketTypeService {
  public resourceUrl = SERVER_API_URL + 'api/ticket-types';

  constructor(protected http: HttpClient) {}

  create(ticketType: ITicketType): Observable<EntityResponseType> {
    return this.http.post<ITicketType>(this.resourceUrl, ticketType, { observe: 'response' });
  }

  update(ticketType: ITicketType): Observable<EntityResponseType> {
    return this.http.put<ITicketType>(this.resourceUrl, ticketType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITicketType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITicketType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
