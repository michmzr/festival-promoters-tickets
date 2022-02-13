import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OrganicPromoTicketsSharedModule } from 'app/shared/shared.module';
import { PromotorComponent } from './promotor.component';
import { PromotorDetailComponent } from './promotor-detail.component';
import { PromotorUpdateComponent } from './promotor-update.component';
import { PromotorDeleteDialogComponent } from './promotor-delete-dialog.component';
import { promotorRoute } from './promotor.route';

@NgModule({
  imports: [OrganicPromoTicketsSharedModule, RouterModule.forChild(promotorRoute)],
  declarations: [PromotorComponent, PromotorDetailComponent, PromotorUpdateComponent, PromotorDeleteDialogComponent],
  entryComponents: [PromotorDeleteDialogComponent],
})
export class OrganicPromoTicketsPromotorModule {}
