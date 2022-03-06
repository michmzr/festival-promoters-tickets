import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'promotor',
        loadChildren: () => import('./promotor/promotor.module').then(m => m.OrganicPromoTicketsPromotorModule),
      },
      {
        path: 'guest',
        loadChildren: () => import('./guest/guest.module').then(m => m.OrganicPromoTicketsGuestModule),
      },
      {
        path: 'ticket',
        loadChildren: () => import('./ticket/ticket.module').then(m => m.OrganicPromoTicketsTicketModule),
      },
      {
        path: 'ticket-type',
        loadChildren: () => import('./ticket-type/ticket-type.module').then(m => m.OrganicPromoTicketsTicketTypeModule),
      },
      {
        path: 'country',
        loadChildren: () => import('./country/country.module').then(m => m.OrganicPromoTicketsCountryModule),
      },
      {
        path: 'promo-code',
        loadChildren: () => import('./promo-code/promo-code.module').then(m => m.OrganicPromoTicketsPromoCodeModule),
      },
      {
        path: 'import-orders',
        loadChildren: () => import('./import/orders/import-order.module').then(m => m.OrganicPromoTicketsPromoImportOrder),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class OrganicPromoTicketsEntityModule {}
