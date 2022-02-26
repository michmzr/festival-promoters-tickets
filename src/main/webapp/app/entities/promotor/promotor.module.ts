import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OrganicPromoTicketsSharedModule } from 'app/shared/shared.module';
import { PromotorComponent } from './promotor.component';
import { PromotorDetailComponent } from './promotor-detail.component';
import { PromotorUpdateComponent } from './promotor-update.component';
import { PromotorDeleteDialogComponent } from './promotor-delete-dialog.component';
import { promotorRoute } from './promotor.route';
import { PromotorCreateComponent } from './promotor-create.component';
import { PromotorPromoNewCodesFormComponent } from './promo-code/promotor-promo-new-codes-form.component';
import { PromotorPromoCodeComponent } from './promo-code/promotor-promo-code.component';

@NgModule({
  imports: [OrganicPromoTicketsSharedModule, RouterModule.forChild(promotorRoute)],
  declarations: [
    PromotorComponent,
    PromotorDetailComponent,
    PromotorUpdateComponent,
    PromotorCreateComponent,
    PromotorDeleteDialogComponent,
    PromotorPromoNewCodesFormComponent,
    PromotorPromoCodeComponent,
  ],
  entryComponents: [PromotorDeleteDialogComponent],
})
export class OrganicPromoTicketsPromotorModule {}
