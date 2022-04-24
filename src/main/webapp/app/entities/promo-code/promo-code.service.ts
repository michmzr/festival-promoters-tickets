import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPromoCode } from 'app/shared/model/promo-code.model';

type EntityResponseType = HttpResponse<IPromoCode>;
type EntityArrayResponseType = HttpResponse<IPromoCode[]>;

@Injectable({ providedIn: 'root' })
export class PromoCodeService {
  public resourceUrl = SERVER_API_URL + 'api/promo-codes';

  constructor(protected http: HttpClient) {}

  create(promoCode: IPromoCode): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promoCode);
    return this.http
      .post<IPromoCode>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(promoCode: IPromoCode): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promoCode);
    return this.http
      .put<IPromoCode>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPromoCode>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findByCode(code: string): Observable<EntityResponseType> {
    return this.http
      .get<IPromoCode>(`${this.resourceUrl}/code/${code}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPromoCode[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(promoCode: IPromoCode): IPromoCode {
    return Object.assign({}, promoCode, {
      createdAt: promoCode.createdAt && promoCode.createdAt.isValid() ? promoCode.createdAt.toJSON() : undefined,
      disabledAt: promoCode.disabledAt && promoCode.disabledAt.isValid() ? promoCode.disabledAt.toJSON() : undefined,
    });
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
      res.body.forEach((promoCode: IPromoCode) => {
        promoCode.createdAt = promoCode.createdAt ? moment(promoCode.createdAt) : undefined;
        promoCode.disabledAt = promoCode.disabledAt ? moment(promoCode.disabledAt) : undefined;
      });
    }
    return res;
  }
}
