import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPromotor, Promotor } from 'app/shared/model/promotor.model';
import { PromotorService } from './promotor.service';
import { PromotorComponent } from './promotor.component';
import { PromotorDetailComponent } from './promotor-detail.component';
import { PromotorUpdateComponent } from './promotor-update.component';
import { PromotorCreateComponent } from './promotor-create.component';

@Injectable({ providedIn: 'root' })
export class PromotorResolve implements Resolve<IPromotor> {
  constructor(private service: PromotorService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPromotor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((promotor: HttpResponse<Promotor>) => {
          if (promotor.body) {
            return of(promotor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Promotor());
  }
}

export const promotorRoute: Routes = [
  {
    path: '',
    component: PromotorComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Promotors',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PromotorDetailComponent,
    resolve: {
      promotor: PromotorResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Promotors',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PromotorCreateComponent,
    resolve: {
      // promotor: new PromotorCreate(),
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Nowy promotor',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PromotorUpdateComponent,
    resolve: {
      promotor: PromotorResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Promotors',
    },
    canActivate: [UserRouteAccessService],
  },
];
