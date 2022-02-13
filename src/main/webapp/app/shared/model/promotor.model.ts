import { Moment } from 'moment';
import { IGuest } from 'app/shared/model/guest.model';
import { IPromoCode } from 'app/shared/model/promo-code.model';

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
  promoCodes?: IPromoCode[];
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
    public guests?: IGuest[],
    public promoCodes?: IPromoCode[]
  ) {
    this.enabled = this.enabled || false;
  }
}

export interface IPromotorCreate {
  id?: number;
  name?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  notes?: string;
  guests?: IGuest[];
  promoCodes?: String[];
}

export class PromotorCreate implements IPromotorCreate {
  constructor(
    public id?: number,
    public name?: string,
    public lastName?: string,
    public email?: string,
    public phoneNumber?: string,
    public notes?: string,
    public guests?: IGuest[],
    public promoCodes?: String[]
  ) {}
}
