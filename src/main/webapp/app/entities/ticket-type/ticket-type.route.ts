import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITicketType, TicketType } from 'app/shared/model/ticket-type.model';
import { TicketTypeService } from './ticket-type.service';
import { TicketTypeComponent } from './ticket-type.component';
import { TicketTypeDetailComponent } from './ticket-type-detail.component';
import { TicketTypeUpdateComponent } from './ticket-type-update.component';

@Injectable({ providedIn: 'root' })
export class TicketTypeResolve implements Resolve<ITicketType> {
  constructor(private service: TicketTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITicketType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((ticketType: HttpResponse<TicketType>) => {
          if (ticketType.body) {
            return of(ticketType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TicketType());
  }
}

export const ticketTypeRoute: Routes = [
  {
    path: '',
    component: TicketTypeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TicketTypes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TicketTypeDetailComponent,
    resolve: {
      ticketType: TicketTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TicketTypes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TicketTypeUpdateComponent,
    resolve: {
      ticketType: TicketTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TicketTypes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TicketTypeUpdateComponent,
    resolve: {
      ticketType: TicketTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TicketTypes',
    },
    canActivate: [UserRouteAccessService],
  },
];
