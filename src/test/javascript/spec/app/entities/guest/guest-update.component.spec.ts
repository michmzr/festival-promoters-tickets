import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { OrganicPromoTicketsTestModule } from '../../../test.module';
import { GuestUpdateComponent } from 'app/entities/guest/guest-update.component';
import { GuestService } from 'app/entities/guest/guest.service';
import { Guest } from 'app/shared/model/guest.model';

describe('Component Tests', () => {
  describe('Guest Management Update Component', () => {
    let comp: GuestUpdateComponent;
    let fixture: ComponentFixture<GuestUpdateComponent>;
    let service: GuestService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OrganicPromoTicketsTestModule],
        declarations: [GuestUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(GuestUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GuestUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GuestService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Guest(123);
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
        const entity = new Guest();
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
