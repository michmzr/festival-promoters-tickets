import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { OrganicPromoTicketsTestModule } from '../../../test.module';
import { PromotorUpdateComponent } from 'app/entities/promotor/promotor-update.component';
import { PromotorService } from 'app/entities/promotor/promotor.service';
import { Promotor } from 'app/shared/model/promotor.model';

describe('Component Tests', () => {
  describe('Promotor Management Update Component', () => {
    let comp: PromotorUpdateComponent;
    let fixture: ComponentFixture<PromotorUpdateComponent>;
    let service: PromotorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OrganicPromoTicketsTestModule],
        declarations: [PromotorUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PromotorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PromotorUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PromotorService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Promotor(123);
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
        const entity = new Promotor();
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
