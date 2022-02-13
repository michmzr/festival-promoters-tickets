import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IGuest, Guest } from 'app/shared/model/guest.model';
import { GuestService } from './guest.service';
import { GuestComponent } from './guest.component';
import { GuestDetailComponent } from './guest-detail.component';
import { GuestUpdateComponent } from './guest-update.component';

@Injectable({ providedIn: 'root' })
export class GuestResolve implements Resolve<IGuest> {
  constructor(private service: GuestService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGuest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((guest: HttpResponse<Guest>) => {
          if (guest.body) {
            return of(guest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Guest());
  }
}

export const guestRoute: Routes = [
  {
    path: '',
    component: GuestComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Guests',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GuestDetailComponent,
    resolve: {
      guest: GuestResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Guests',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GuestUpdateComponent,
    resolve: {
      guest: GuestResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Guests',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GuestUpdateComponent,
    resolve: {
      guest: GuestResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Guests',
    },
    canActivate: [UserRouteAccessService],
  },
];
