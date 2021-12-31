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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class OrganicPromoTicketsEntityModule {}
