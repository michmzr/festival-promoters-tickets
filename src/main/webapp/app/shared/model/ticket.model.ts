import {Moment} from 'moment';
import {IGuest} from './guest.model';
import {ITicketType} from './ticket-type.model';
import {IPromotor} from './promotor.model';

export interface ITicket {
  id?: number;
  name?: string;
  uuid?: string;
  ticketUrl?: string;
  ticketQRContentType?: string;
  ticketQR?: string;
  ticketFileContentType?: string;
  ticketFile?: any;
  enabled?: boolean;
  createdAt?: Moment;
  disabledAt?: Moment;
  ticketTypeId?: number;
  promoCodeId?: number;
  ticketPrice?: string;
  guestId?: number;
  orderId?: string;
}

export class Ticket implements ITicket {
  constructor(
    public id?: number,
    public name?: string,
    public uuid?: string,
    public ticketUrl?: string,
    public ticketQRContentType?: string,
    public ticketQR?: string,
    public ticketFileContentType?: string,
    public ticketFile?: any,
    public enabled?: boolean,
    public createdAt?: Moment,
    public disabledAt?: Moment,
    public ticketTypeId?: number,
    public ticketPrice?: string,
    public promoCodeId?: number,
    public guestId?: number,
    public orderId?: string
  ) {
    this.enabled = this.enabled || false;
  }
}

export class TicketCreate {
  constructor(
    public ticketTypeId?: number,
    public ticketPrice?: string,
    public promoCodeId?: number,
    public guestId?: number,
    public promotorId?: number,
    public orderId?: string
  ) {
  }
}

export enum VerificationStatus {
  OK = 'OK',
  ALREADY_VALIDATED = 'ALREADY_VALIDATED',
  DEACTIVATED = 'DEACTIVATED',
  NOT_FOUND = 'NOT_FOUND',
}

export interface ITicketVerificationStatus {
  status: VerificationStatus;
  guest?: IGuest;
  ticketType?: ITicketType;
  promotor?: IPromotor;
  message?: String;
}

export class TicketVerificationStatus implements ITicketVerificationStatus {
  constructor(
    public status: VerificationStatus,
    public guest?: IGuest,
    public ticketType?: ITicketType,
    public promotor?: IPromotor,
    public message?: String
  ) {}
}
