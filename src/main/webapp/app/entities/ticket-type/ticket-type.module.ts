import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OrganicPromoTicketsSharedModule } from 'app/shared/shared.module';
import { TicketTypeComponent } from './ticket-type.component';
import { TicketTypeDetailComponent } from './ticket-type-detail.component';
import { TicketTypeUpdateComponent } from './ticket-type-update.component';
import { TicketTypeDeleteDialogComponent } from './ticket-type-delete-dialog.component';
import { ticketTypeRoute } from './ticket-type.route';

@NgModule({
  imports: [OrganicPromoTicketsSharedModule, RouterModule.forChild(ticketTypeRoute)],
  declarations: [TicketTypeComponent, TicketTypeDetailComponent, TicketTypeUpdateComponent, TicketTypeDeleteDialogComponent],
  entryComponents: [TicketTypeDeleteDialogComponent],
})
export class OrganicPromoTicketsTicketTypeModule {}
