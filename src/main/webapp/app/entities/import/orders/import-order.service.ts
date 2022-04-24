import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IImportOrderRequest, IOrdersImportResult } from '../../../shared/model/import-order.model';

@Injectable({ providedIn: 'root' })
export class ImportOrderService {
  public resourceUrl = SERVER_API_URL + 'api/import/orders';

  constructor(protected http: HttpClient) {}

  import(payload: IImportOrderRequest): Observable<HttpResponse<IOrdersImportResult>> {
    const formPayload = new FormData();

    const file: File = payload.file;
    formPayload.append('file', file);

    return this.http.post<IOrdersImportResult>(this.resourceUrl, formPayload, {
      reportProgress: true,
      observe: 'response',
    });
  }
}
