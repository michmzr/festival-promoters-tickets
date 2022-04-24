import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';

import {ITicketType, TicketType} from 'app/shared/model/ticket-type.model';
import {TicketTypeService} from './ticket-type.service';

@Component({
  selector: 'jhi-ticket-type-update',
  templateUrl: './ticket-type-update.component.html',
})
export class TicketTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(200)]],
    notes: [null, [Validators.maxLength(500)]],
    productId: [null, [Validators.maxLength(50)]],
    productUrl: [null, [Validators.maxLength(250)]],
  });

  constructor(protected ticketTypeService: TicketTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ticketType }) => {
      this.updateForm(ticketType);
    });
  }

  updateForm(ticketType: ITicketType): void {
    this.editForm.patchValue({
      id: ticketType.id,
      name: ticketType.name,
      notes: ticketType.notes,
      productId: ticketType.productId,
      productUrl: ticketType.productUrl,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ticketType = this.createFromForm();
    if (ticketType.id !== undefined) {
      this.subscribeToSaveResponse(this.ticketTypeService.update(ticketType));
    } else {
      this.subscribeToSaveResponse(this.ticketTypeService.create(ticketType));
    }
  }

  private createFromForm(): ITicketType {
    return {
      ...new TicketType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      productId: this.editForm.get(['productId'])!.value,
      productUrl: this.editForm.get(['productUrl'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITicketType>>): void {
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
