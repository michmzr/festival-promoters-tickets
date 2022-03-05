import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { OrganicPromoTicketsTestModule } from '../../../test.module';
import { PromoCodeUpdateComponent } from 'app/entities/promo-code/promo-code-update.component';
import { PromoCodeService } from 'app/entities/promo-code/promo-code.service';
import { PromoCode } from 'app/shared/model/promo-code.model';

describe('Component Tests', () => {
  describe('PromoCode Management Update Component', () => {
    let comp: PromoCodeUpdateComponent;
    let fixture: ComponentFixture<PromoCodeUpdateComponent>;
    let service: PromoCodeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OrganicPromoTicketsTestModule],
        declarations: [PromoCodeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PromoCodeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PromoCodeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PromoCodeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PromoCode(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new PromoCode();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
