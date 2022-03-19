import { Moment } from 'moment';

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
    public promoCodeId?: number,
    public guestId?: number,
    public orderId?: string
  ) {
    this.enabled = this.enabled || false;
  }
}
