import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { OrganicPromoTicketsTestModule } from '../../../test.module';
import { TicketTypeUpdateComponent } from 'app/entities/ticket-type/ticket-type-update.component';
import { TicketTypeService } from 'app/entities/ticket-type/ticket-type.service';
import { TicketType } from 'app/shared/model/ticket-type.model';

describe('Component Tests', () => {
  describe('TicketType Management Update Component', () => {
    let comp: TicketTypeUpdateComponent;
    let fixture: ComponentFixture<TicketTypeUpdateComponent>;
    let service: TicketTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OrganicPromoTicketsTestModule],
        declarations: [TicketTypeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TicketTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TicketTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TicketTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TicketType(123);
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
        const entity = new TicketType();
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
