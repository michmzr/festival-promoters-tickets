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
  guest?: IGuest;
  ticket?: ITicket;

  orderRecord?: Object;
  validation?: IValidationResult;

  messages?: String[];

  processed?: boolean;
}

export class OrderImportResult implements IOrderImportResult {
  constructor(
    public guest: IGuest,
    public messages: String[],
    public orderRecord: Object,
    public ticket: ITicket,
    public validation: IValidationResult,
    public processed: boolean
  ) {
    this.guest = guest;
    this.messages = messages;
    this.orderRecord = orderRecord;
    this.ticket = ticket;
    this.validation = validation;
    this.processed = processed;
  }
}

export interface IOrdersImportResult {
  imported: number;
  failed: number;

  results: IOrderImportResult[];
  errors: String[];
}

export class OrdersImportResult implements IOrdersImportResult {
  constructor(public failed: number, public imported: number, public errors: String[], public results: IOrderImportResult[]) {
    this.errors = errors;
    this.failed = failed;
    this.imported = imported;
    this.results = results;
  }
}
