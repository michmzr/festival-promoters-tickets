import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPromotor, Promotor } from 'app/shared/model/promotor.model';
import { PromotorService } from './promotor.service';

@Component({
  selector: 'jhi-promotor-update',
  templateUrl: './promotor-update.component.html',
})
export class PromotorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    email: [],
    phoneNumber: [],
    notes: [null, [Validators.maxLength(500)]],
    createdAt: [],
    enabled: [null, [Validators.required]],
  });

  constructor(protected promotorService: PromotorService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promotor }) => {
      if (!promotor.id) {
        const today = moment().startOf('day');
        promotor.createdAt = today;
      }

      this.updateForm(promotor);
    });
  }

  updateForm(promotor: IPromotor): void {
    this.editForm.patchValue({
      id: promotor.id,
      name: promotor.name,
      lastName: promotor.lastName,
      email: promotor.email,
      phoneNumber: promotor.phoneNumber,
      notes: promotor.notes,
      createdAt: promotor.createdAt ? promotor.createdAt.format(DATE_TIME_FORMAT) : null,
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
      this.subscribeToSaveResponse(this.promotorService.create(promotor));
    }
  }

  private createFromForm(): IPromotor {
    return {
      ...new Promotor(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      enabled: this.editForm.get(['enabled'])!.value,
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
}
