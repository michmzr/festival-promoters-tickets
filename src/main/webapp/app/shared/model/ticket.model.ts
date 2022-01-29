import { Moment } from 'moment';

export interface ITicket {
  id?: number;
  name?: string;
  uuid?: string;
  ticketUrl?: string;
  ticketQRContentType?: string;
  ticketQR?: any;
  ticketFileContentType?: string;
  ticketFile?: any;
  enabled?: boolean;
  createdAt?: Moment;
  disabledAt?: Moment;
  ticketTypeId?: number;
  guestId?: number;
}

export class Ticket implements ITicket {
  constructor(
    public id?: number,
    public name?: string,
    public uuid?: string,
    public ticketUrl?: string,
    public ticketQRContentType?: string,
    public ticketQR?: any,
    public ticketFileContentType?: string,
    public ticketFile?: any,
    public enabled?: boolean,
    public createdAt?: Moment,
    public disabledAt?: Moment,
    public ticketTypeId?: number,
    public guestId?: number
  ) {
    this.enabled = this.enabled || false;
  }
}
