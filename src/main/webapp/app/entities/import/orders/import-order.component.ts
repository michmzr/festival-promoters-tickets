import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { ImportOrderService } from './import-order.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { IImportOrderRequest, IOrdersImportResult } from '../../../shared/model/import-order.model';

@Component({
  selector: 'jhi-import-order',
  templateUrl: './import-order-component.html',
  styleUrls: ['./import-order.component.scss'],
})
export class ImportOrderComponent implements OnInit, OnDestroy {
  isSaving = false;
  editForm = this.fb.group({
    uploadFile: [null, [Validators.required]],
  });

  fileName: String = 'Choose file';
  @ViewChild('fileInput') fileInput?: ElementRef;
  fileAttr: String = 'Choose File';

  importResults?: IOrdersImportResult;

  constructor(protected importOrderService: ImportOrderService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnDestroy(): void {}

  ngOnInit(): void {
    /* const lastImport = localStorage.getItem('import-order-last-results');
     if (lastImport) {
       this.importResults = JSON.parse(lastImport);
     }*/
  }

  uploadFileEvt(eventFile: any): void {
    this.importResults = undefined;

    if (eventFile.target.files && eventFile.target.files[0]) {
      this.fileAttr = '';

      Array.from(eventFile.target.files).forEach((file: any) => {
        this.fileAttr += file.name;
      });

      // HTML5 FileReader API
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const image = new Image();
        image.src = e.target.result;
      };

      reader.readAsDataURL(eventFile.target.files[0]);

      this.editForm.patchValue({
        uploadFile: eventFile.target.files[0],
      });

      // Reset if duplicate image uploaded again
      if (this.fileInput) this.fileInput.nativeElement.value = '';
    } else {
      this.fileAttr = 'Choose File';
    }
  }

  private createFromForm(): IImportOrderRequest {
    return {
      file: this.editForm.get(['uploadFile'])!.value,
    };
  }

  importOrders(): void {
    this.isSaving = true;

    const payload: IImportOrderRequest = this.createFromForm();
    this.subscribeToImportResponse(this.importOrderService.import(payload));
  }

  protected subscribeToImportResponse(resultObs: Observable<HttpResponse<IOrdersImportResult>>): void {
    resultObs.subscribe(
      (httpResp: HttpResponse<IOrdersImportResult>) => this.onSuccess(httpResp.body!),
      error => this.onError(error)
    );
  }

  protected onSuccess(results: IOrdersImportResult | undefined): void {
    this.importResults = results;
    console.info(this.importResults);

    this.fileName = 'Choice file';
    this.editForm.patchValue({
      uploadFile: null,
    });

    localStorage.setItem('import-order-last-results', JSON.stringify(this.importResults));

    this.isSaving = false;
  }

  protected onError(error: any): void {
    console.error(error);

    this.isSaving = false;
  }
}
