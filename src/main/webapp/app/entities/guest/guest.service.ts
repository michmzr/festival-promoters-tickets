import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IGuest } from 'app/shared/model/guest.model';

type EntityResponseType = HttpResponse<IGuest>;
type EntityArrayResponseType = HttpResponse<IGuest[]>;

@Injectable({ providedIn: 'root' })
export class GuestService {
  public resourceUrl = SERVER_API_URL + 'api/guests';

  constructor(protected http: HttpClient) {}

  create(guest: IGuest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(guest);
    return this.http
      .post<IGuest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(guest: IGuest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(guest);
    return this.http
      .put<IGuest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGuest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGuest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(guest: IGuest): IGuest {
    const copy: IGuest = Object.assign({}, guest, {
      createdAt: guest.createdAt && guest.createdAt.isValid() ? guest.createdAt.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((guest: IGuest) => {
        guest.createdAt = guest.createdAt ? moment(guest.createdAt) : undefined;
      });
    }
    return res;
  }
}
