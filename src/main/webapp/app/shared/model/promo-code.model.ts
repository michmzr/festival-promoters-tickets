import { Moment } from 'moment';

export interface IPromoCode {
  id?: number;
  code?: string;
  notes?: string;
  enabled?: boolean;
  createdAt?: Moment;
  disabledAt?: Moment;
  ticketId?: number;
  promotorId?: number;
}

export class PromoCode implements IPromoCode {
  constructor(
    public id?: number,
    public code?: string,
    public notes?: string,
    public enabled?: boolean,
    public createdAt?: Moment,
    public disabledAt?: Moment,
    public ticketId?: number,
    public promotorId?: number
  ) {
    this.enabled = this.enabled || false;
  }
}
