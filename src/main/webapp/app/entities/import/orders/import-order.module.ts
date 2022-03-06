import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OrganicPromoTicketsSharedModule } from 'app/shared/shared.module';
import { importOrderRoute } from './import-order.route';
import { ImportOrderComponent } from './import-order.component';

@NgModule({
  imports: [OrganicPromoTicketsSharedModule, RouterModule.forChild(importOrderRoute)],
  declarations: [ImportOrderComponent],
  entryComponents: [],
})
export class OrganicPromoTicketsPromoImportOrder {}
