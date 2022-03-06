import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IImportOrderRequest } from '../../../shared/model/import-oder.model';

type EntityResponseType = HttpResponse<Object>;
type EntityArrayResponseType = HttpResponse<Object[]>;

@Injectable({ providedIn: 'root' })
export class ImportOrderService {
  public resourceUrl = SERVER_API_URL + 'api/import/orders';

  constructor(protected http: HttpClient) {}

  import(payload: IImportOrderRequest): Observable<EntityResponseType> {
    console.info('payload: ' + payload);

    const formPayload = new FormData();
    formPayload.append('file', payload?.file);

    return this.http.post<Object>(this.resourceUrl, formPayload, {
      headers: new HttpHeaders({
        'Content-Type': 'multipart/form-data',
      }),
      observe: 'response',
    });
  }
}
