import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {OrganicPromoTicketsSharedModule} from 'app/shared/shared.module';
import {TicketComponent} from './ticket.component';
import {TicketDetailComponent} from './ticket-detail.component';
import {TicketUpdateComponent} from './ticket-update.component';
import {TicketDeleteDialogComponent} from './ticket-delete-dialog.component';
import {ticketRoute} from './ticket.route';
import {TicketCreateComponent} from "./ticket-create.component";

@NgModule({
  imports: [OrganicPromoTicketsSharedModule, RouterModule.forChild(ticketRoute)],
  declarations: [TicketComponent, TicketDetailComponent, TicketCreateComponent, TicketUpdateComponent, TicketDeleteDialogComponent],
  entryComponents: [TicketDeleteDialogComponent],
})
export class OrganicPromoTicketsTicketModule {
}
