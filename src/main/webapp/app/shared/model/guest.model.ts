import { Moment } from 'moment';

export interface IGuest {
  id?: number;
  name?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  notes?: string;
  createdAt?: Moment;
  enabled?: boolean;
  ticketId?: number;
  ticketTypeId?: number;
  promotorId?: number;
}

export class Guest implements IGuest {
  constructor(
    public id?: number,
    public name?: string,
    public lastName?: string,
    public email?: string,
    public phoneNumber?: string,
    public notes?: string,
    public createdAt?: Moment,
    public enabled?: boolean,
    public ticketId?: number,
    public ticketTypeId?: number,
    public promotorId?: number
  ) {
    this.enabled = this.enabled || false;
  }
}
