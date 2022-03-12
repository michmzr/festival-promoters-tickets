import { IGuest } from './guest.model';
import { ITicket } from './ticket.model';

export interface IImportOrderRequest {
  file: File;
}

export class ImportOrderRequest implements IImportOrderRequest {
  public file: File;

  constructor(file: File) {
    this.file = file;
  }
}

export interface IValidationResult {
  messages: String[];
  fieldNames: String[];
  valid: boolean;
}

export class ValidationResult implements IValidationResult {
  constructor(public fieldNames: String[], public messages: String[], public valid: boolean) {
    this.fieldNames = fieldNames;
    this.messages = messages;
    this.valid = valid;
  }
}

export interface IOrderImportResult {
  guestDTO?: IGuest;
  ticketDTO?: ITicket;

  orderRecord?: Object;
  validation?: IValidationResult;

  messages?: String[];
}

export class OrderImportResult implements IOrderImportResult {
  constructor(
    public guestDTO: IGuest,
    public messages: String[],
    public orderRecord: Object,
    public ticketDTO: ITicket,
    public validation: IValidationResult
  ) {
    this.guestDTO = guestDTO;
    this.messages = messages;
    this.orderRecord = orderRecord;
    this.ticketDTO = ticketDTO;
    this.validation = validation;
  }
}

export interface IOrdersImportResult {
  results: IImportOrderRequest[];
  errors: String[];
  imported: number;
  failed: number;
}

export class OrdersImportResult implements IOrdersImportResult {
  constructor(public errors: String[], public failed: number, public imported: number, public results: IImportOrderRequest[]) {
    this.errors = errors;
    this.failed = failed;
    this.imported = imported;
    this.results = results;
  }
}
