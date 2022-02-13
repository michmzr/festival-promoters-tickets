import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPromoCode, PromoCode } from 'app/shared/model/promo-code.model';
import { PromoCodeService } from './promo-code.service';
import { PromoCodeComponent } from './promo-code.component';
import { PromoCodeDetailComponent } from './promo-code-detail.component';
import { PromoCodeUpdateComponent } from './promo-code-update.component';

@Injectable({ providedIn: 'root' })
export class PromoCodeResolve implements Resolve<IPromoCode> {
  constructor(private service: PromoCodeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPromoCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((promoCode: HttpResponse<PromoCode>) => {
          if (promoCode.body) {
            return of(promoCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PromoCode());
  }
}

export const promoCodeRoute: Routes = [
  {
    path: '',
    component: PromoCodeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PromoCodes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PromoCodeDetailComponent,
    resolve: {
      promoCode: PromoCodeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PromoCodes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PromoCodeUpdateComponent,
    resolve: {
      promoCode: PromoCodeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PromoCodes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PromoCodeUpdateComponent,
    resolve: {
      promoCode: PromoCodeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PromoCodes',
    },
    canActivate: [UserRouteAccessService],
  },
];
