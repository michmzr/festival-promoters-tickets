import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrganicPromoTicketsTestModule } from '../../../test.module';
import { PromoCodeDetailComponent } from 'app/entities/promo-code/promo-code-detail.component';
import { PromoCode } from 'app/shared/model/promo-code.model';

describe('Component Tests', () => {
  describe('PromoCode Management Detail Component', () => {
    let comp: PromoCodeDetailComponent;
    let fixture: ComponentFixture<PromoCodeDetailComponent>;
    const route = ({ data: of({ promoCode: new PromoCode(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OrganicPromoTicketsTestModule],
        declarations: [PromoCodeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PromoCodeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PromoCodeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load promoCode on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.promoCode).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
