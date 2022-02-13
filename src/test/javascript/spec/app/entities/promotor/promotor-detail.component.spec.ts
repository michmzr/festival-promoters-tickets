import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrganicPromoTicketsTestModule } from '../../../test.module';
import { PromotorDetailComponent } from 'app/entities/promotor/promotor-detail.component';
import { Promotor } from 'app/shared/model/promotor.model';

describe('Component Tests', () => {
  describe('Promotor Management Detail Component', () => {
    let comp: PromotorDetailComponent;
    let fixture: ComponentFixture<PromotorDetailComponent>;
    const route = ({ data: of({ promotor: new Promotor(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OrganicPromoTicketsTestModule],
        declarations: [PromotorDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PromotorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PromotorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load promotor on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.promotor).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
