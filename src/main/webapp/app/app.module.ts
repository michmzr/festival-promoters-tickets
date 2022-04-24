import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import './vendor';
import { OrganicPromoTicketsSharedModule } from 'app/shared/shared.module';
import { OrganicPromoTicketsCoreModule } from 'app/core/core.module';
import { OrganicPromoTicketsAppRoutingModule } from './app-routing.module';
import { OrganicPromoTicketsHomeModule } from './home/home.module';
import { OrganicPromoTicketsEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TicketVerifyComponent } from './entities/ticket/ticket-verify/ticket-verify.component';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    OrganicPromoTicketsSharedModule,
    OrganicPromoTicketsCoreModule,
    OrganicPromoTicketsHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    OrganicPromoTicketsEntityModule,
    OrganicPromoTicketsAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent, TicketVerifyComponent],
  bootstrap: [MainComponent],
})
export class OrganicPromoTicketsAppModule {}
