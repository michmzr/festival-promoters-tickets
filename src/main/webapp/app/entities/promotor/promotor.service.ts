import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPromotor, IPromotorCreate } from 'app/shared/model/promotor.model';

type EntityResponseType = HttpResponse<IPromotor>;
type EntityArrayResponseType = HttpResponse<IPromotor[]>;

@Injectable({ providedIn: 'root' })
export class PromotorService {
  public resourceUrl = SERVER_API_URL + 'api/promotors';

  constructor(protected http: HttpClient) {}

  create(promotor: IPromotorCreate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClientForCreate(promotor);
    return this.http
      .post<IPromotor>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(promotor: IPromotorCreate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promotor);
    return this.http
      .put<IPromotor>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPromotor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPromotor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(promotor: IPromotor): IPromotor {
    const copy: IPromotor = Object.assign({}, promotor, {
      createdAt: promotor.createdAt && promotor.createdAt.isValid() ? promotor.createdAt.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromClientForCreate(promotor: IPromotorCreate): IPromotorCreate {
    const copy: IPromotorCreate = Object.assign({}, promotor, {});
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body && res.body.createdAt) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((promotor: IPromotor) => {
        promotor.createdAt = promotor.createdAt ? moment(promotor.createdAt) : undefined;
      });
    }
    return res;
  }
}
