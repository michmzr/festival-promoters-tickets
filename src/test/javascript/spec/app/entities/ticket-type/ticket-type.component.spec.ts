import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { OrganicPromoTicketsTestModule } from '../../../test.module';
import { TicketTypeComponent } from 'app/entities/ticket-type/ticket-type.component';
import { TicketTypeService } from 'app/entities/ticket-type/ticket-type.service';
import { TicketType } from 'app/shared/model/ticket-type.model';

describe('Component Tests', () => {
  describe('TicketType Management Component', () => {
    let comp: TicketTypeComponent;
    let fixture: ComponentFixture<TicketTypeComponent>;
    let service: TicketTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OrganicPromoTicketsTestModule],
        declarations: [TicketTypeComponent],
      })
        .overrideTemplate(TicketTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TicketTypeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TicketTypeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new TicketType(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.ticketTypes && comp.ticketTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
