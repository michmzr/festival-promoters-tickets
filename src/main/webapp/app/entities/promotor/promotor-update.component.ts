import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { IPromotor, IPromotorCreate, PromotorCreate } from 'app/shared/model/promotor.model';
import { PromotorService } from './promotor.service';
import { IPromoCode } from '../../shared/model/promo-code.model';
import { PromoCodeService } from '../promo-code/promo-code.service';

export interface Fruit {
  name: string;
}

@Component({
  selector: 'jhi-promotor-update',
  templateUrl: './promotor-update.component.html',
})
export class PromotorUpdateComponent implements OnInit {
  isSaving = false;

  promotor?: IPromotor;
  promoCodes?: IPromoCode[];

  newPromoCodes: string[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    email: [null],
    phoneNumber: [null],
    notes: [null, [Validators.maxLength(500)]],
    createdAt: [],
    enabled: [null, [Validators.required]],
  });

  constructor(
    protected promotorService: PromotorService,
    protected promoCodeService: PromoCodeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promotor }) => {
      if (!promotor.id) {
        promotor.createdAt = moment().startOf('day');
      }

      this.updateForm(promotor);
    });
  }

  updateForm(promotor: IPromotor): void {
    this.promotor = promotor;
    this.promoCodes = promotor.promoCodes;

    this.editForm.patchValue({
      id: promotor.id,
      name: promotor.name,
      lastName: promotor.lastName,
      email: promotor.email,
      phoneNumber: promotor.phoneNumber,
      notes: promotor.notes,
      enabled: promotor.enabled,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;

    const promotor = this.createFromForm();
    if (promotor.id !== undefined) {
      this.subscribeToSaveResponse(this.promotorService.update(promotor));
    } else {
      console.error('promotor has no id!');
    }
  }

  private createFromForm(): IPromotorCreate {
    return {
      ...new PromotorCreate(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      newPromoCodes: this.newPromoCodes,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPromotor>>): void {
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

  codeAdded(code: string): void {
    console.info(`code ${code} added`);
    this.newPromoCodes.push(code);
  }

  codeRemoved(code: string): void {
    console.info(`code ${code} removed`);

    const index = this.newPromoCodes.indexOf(code);

    if (index >= 0) {
      this.newPromoCodes.splice(index, 1);
    }
  }
}
