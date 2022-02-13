import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrganicPromoTicketsTestModule } from '../../../test.module';
import { GuestDetailComponent } from 'app/entities/guest/guest-detail.component';
import { Guest } from 'app/shared/model/guest.model';

describe('Component Tests', () => {
  describe('Guest Management Detail Component', () => {
    let comp: GuestDetailComponent;
    let fixture: ComponentFixture<GuestDetailComponent>;
    const route = ({ data: of({ guest: new Guest(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OrganicPromoTicketsTestModule],
        declarations: [GuestDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(GuestDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GuestDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load guest on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.guest).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
