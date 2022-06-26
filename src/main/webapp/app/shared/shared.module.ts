import { NgModule } from '@angular/core';
import { OrganicPromoTicketsSharedLibsModule } from './shared-libs.module';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { LoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { JhMaterialModule } from 'app/shared/jh-material.module';
import { FormBooleanLabelComponent } from 'app/shared/form/form-boolean-label-component';

@NgModule({
  imports: [OrganicPromoTicketsSharedLibsModule, JhMaterialModule],
  declarations: [AlertComponent, AlertErrorComponent, LoginModalComponent, HasAnyAuthorityDirective, FormBooleanLabelComponent],
  entryComponents: [LoginModalComponent],
  exports: [
    OrganicPromoTicketsSharedLibsModule,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    JhMaterialModule,
    FormBooleanLabelComponent,
  ],
})
export class OrganicPromoTicketsSharedModule {}
