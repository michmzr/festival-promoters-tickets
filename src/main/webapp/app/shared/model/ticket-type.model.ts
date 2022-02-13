export interface ITicketType {
  id?: number;
  name?: string;
  notes?: string;
}

export class TicketType implements ITicketType {
  constructor(public id?: number, public name?: string, public notes?: string) {}
}
