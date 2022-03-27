import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ITicketVerificationStatus } from 'app/shared/model/ticket.model';

type EntityResponseType = HttpResponse<ITicketVerificationStatus>;

@Injectable({ providedIn: 'root' })
export class TicketVerifyService {
  public resourceUrl = SERVER_API_URL + 'api/ticket/verify/';

  constructor(protected http: HttpClient) {}

  find(uuid: String): Observable<EntityResponseType> {
    const url = `${this.resourceUrl}${uuid}`;

    console.info(`Calling ${url}....`);

    return this.http
      .get<ITicketVerificationStatus>(url, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    /* if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
      res.body.disabledAt = res.body.disabledAt ? moment(res.body.disabledAt) : undefined;
    } */
    return res;
  }
}
