import { Routes } from '@angular/router';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ImportOrderComponent } from './import-order.component';

export const importOrderRoute: Routes = [
  {
    path: '',
    component: ImportOrderComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Import order',
    },
    canActivate: [UserRouteAccessService],
  },
];
