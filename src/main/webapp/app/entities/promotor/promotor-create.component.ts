import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPromotor, IPromotorCreate, PromotorCreate } from 'app/shared/model/promotor.model';
import { PromotorService } from './promotor.service';

@Component({
  selector: 'jhi-promotor-create',
  templateUrl: './promotor-create.component.html',
})
export class PromotorCreateComponent implements OnInit {
  isSaving = false;

  newPromoCodes: string[] = [];

  createForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    email: [null, [Validators.required]],
    phoneNumber: [null],
    notes: [null, [Validators.maxLength(500)]],
  });

  constructor(protected promotorService: PromotorService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    /*  this.activatedRoute.data.subscribe(({ promotor }) => {
      if (!promotor.id) {
        promotor.createdAt = moment().startOf('day');
      }

    });*/
    this.createFromForm();
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;

    const promotor = this.createFromForm();
    this.subscribeToSaveResponse(this.promotorService.create(promotor));
  }

  private createFromForm(): IPromotorCreate {
    return {
      ...new PromotorCreate(),
      id: this.createForm.get(['id'])!.value,
      name: this.createForm.get(['name'])!.value,
      lastName: this.createForm.get(['lastName'])!.value,
      email: this.createForm.get(['email'])!.value,
      phoneNumber: this.createForm.get(['phoneNumber'])!.value,
      notes: this.createForm.get(['notes'])!.value,
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
