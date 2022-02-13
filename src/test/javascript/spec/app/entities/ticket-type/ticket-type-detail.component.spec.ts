import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrganicPromoTicketsTestModule } from '../../../test.module';
import { TicketTypeDetailComponent } from 'app/entities/ticket-type/ticket-type-detail.component';
import { TicketType } from 'app/shared/model/ticket-type.model';

describe('Component Tests', () => {
  describe('TicketType Management Detail Component', () => {
    let comp: TicketTypeDetailComponent;
    let fixture: ComponentFixture<TicketTypeDetailComponent>;
    const route = ({ data: of({ ticketType: new TicketType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OrganicPromoTicketsTestModule],
        declarations: [TicketTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TicketTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TicketTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ticketType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ticketType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
