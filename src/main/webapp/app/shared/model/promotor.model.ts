import { Moment } from 'moment';
import { IGuest } from 'app/shared/model/guest.model';

export interface IPromotor {
  id?: number;
  name?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  notes?: string;
  createdAt?: Moment;
  enabled?: boolean;
  guests?: IGuest[];
}

export class Promotor implements IPromotor {
  constructor(
    public id?: number,
    public name?: string,
    public lastName?: string,
    public email?: string,
    public phoneNumber?: string,
    public notes?: string,
    public createdAt?: Moment,
    public enabled?: boolean,
    public guests?: IGuest[]
  ) {
    this.enabled = this.enabled || false;
  }
}
