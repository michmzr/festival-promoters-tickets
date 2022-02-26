import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPromoCode, PromoCode } from 'app/shared/model/promo-code.model';
import { PromoCodeService } from './promo-code.service';
import { IPromotor } from 'app/shared/model/promotor.model';
import { PromotorService } from 'app/entities/promotor/promotor.service';

@Component({
  selector: 'jhi-promo-code-update',
  templateUrl: './promo-code-update.component.html',
})
export class PromoCodeUpdateComponent implements OnInit {
  isSaving = false;
  promotors: IPromotor[] = [];

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required, Validators.maxLength(30)]],
    notes: [null, [Validators.maxLength(500)]],
    /*  enabled: [null, [Validators.required]],
    createdAt: [null, [Validators.required]],
    disabledAt: [],*/
    promotorId: [],
  });

  constructor(
    protected promoCodeService: PromoCodeService,
    protected promotorService: PromotorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promoCode }) => {
      if (!promoCode.id) {
        const today = moment().startOf('day');
        promoCode.createdAt = today;
        promoCode.disabledAt = today;
      }

      this.updateForm(promoCode);

      this.promotorService.query().subscribe((res: HttpResponse<IPromotor[]>) => (this.promotors = res.body || []));
    });
  }

  updateForm(promoCode: IPromoCode): void {
    this.editForm.patchValue({
      id: promoCode.id,
      code: promoCode.code,
      notes: promoCode.notes,
      enabled: promoCode.enabled,
      createdAt: promoCode.createdAt ? promoCode.createdAt.format(DATE_TIME_FORMAT) : null,
      disabledAt: promoCode.disabledAt ? promoCode.disabledAt.format(DATE_TIME_FORMAT) : null,
      promotorId: promoCode.promotorId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const promoCode = this.createFromForm();
    if (promoCode.id !== undefined) {
      this.subscribeToSaveResponse(this.promoCodeService.update(promoCode));
    } else {
      this.subscribeToSaveResponse(this.promoCodeService.create(promoCode));
    }
  }

  private createFromForm(): IPromoCode {
    return {
      ...new PromoCode(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      enabled: this.editForm.get(['enabled'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      disabledAt: this.editForm.get(['disabledAt'])!.value ? moment(this.editForm.get(['disabledAt'])!.value, DATE_TIME_FORMAT) : undefined,
      promotorId: this.editForm.get(['promotorId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPromoCode>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IPromotor): any {
    return item.id;
  }
}
