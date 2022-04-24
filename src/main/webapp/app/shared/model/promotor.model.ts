import { Moment } from 'moment';
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
  newPromoCodes?: String[];
}

export class PromotorCreate implements IPromotorCreate {
  constructor(
    public id?: number,
    public name?: string,
    public lastName?: string,
    public email?: string,
    public phoneNumber?: string,
    public notes?: string,
    public newPromoCodes?: String[]
  ) {}
}
