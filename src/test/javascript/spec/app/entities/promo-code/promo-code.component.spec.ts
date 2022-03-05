import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { OrganicPromoTicketsTestModule } from '../../../test.module';
import { PromoCodeComponent } from 'app/entities/promo-code/promo-code.component';
import { PromoCodeService } from 'app/entities/promo-code/promo-code.service';
import { PromoCode } from 'app/shared/model/promo-code.model';

describe('Component Tests', () => {
  describe('PromoCode Management Component', () => {
    let comp: PromoCodeComponent;
    let fixture: ComponentFixture<PromoCodeComponent>;
    let service: PromoCodeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OrganicPromoTicketsTestModule],
        declarations: [PromoCodeComponent],
      })
        .overrideTemplate(PromoCodeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PromoCodeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PromoCodeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PromoCode(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.promoCodes && comp.promoCodes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
