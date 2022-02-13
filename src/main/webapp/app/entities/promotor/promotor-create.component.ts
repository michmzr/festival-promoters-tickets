import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPromotor, IPromotorCreate, PromotorCreate } from 'app/shared/model/promotor.model';
import { PromotorService } from './promotor.service';

@Component({
  selector: 'jhi-promotor-create',
  templateUrl: './promotor-create.component.html',
})
export class PromotorCreateComponent implements OnInit {
  isSaving = false;

  createForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    email: [],
    phoneNumber: [],
    notes: [null, [Validators.maxLength(500)]],
    createdAt: [],
    enabled: [null, [Validators.required]],
    promoCodes: [],
  });

  constructor(protected promotorService: PromotorService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    /*  this.activatedRoute.data.subscribe(({ promotor }) => {
      if (!promotor.id) {
        promotor.createdAt = moment().startOf('day');
      }

      this.updateForm(promotor);
    });*/
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
      promoCodes: this.createForm.get(['promoCodes'])!.value,
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
