import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OrganicPromoTicketsSharedModule } from 'app/shared/shared.module';
import { PromoCodeComponent } from './promo-code.component';
import { PromoCodeDetailComponent } from './promo-code-detail.component';
import { PromoCodeUpdateComponent } from './promo-code-update.component';
import { PromoCodeDeleteDialogComponent } from './promo-code-delete-dialog.component';
import { promoCodeRoute } from './promo-code.route';

@NgModule({
  imports: [OrganicPromoTicketsSharedModule, RouterModule.forChild(promoCodeRoute)],
  declarations: [PromoCodeComponent, PromoCodeDetailComponent, PromoCodeUpdateComponent, PromoCodeDeleteDialogComponent],
  entryComponents: [PromoCodeDeleteDialogComponent],
})
export class OrganicPromoTicketsPromoCodeModule {}
