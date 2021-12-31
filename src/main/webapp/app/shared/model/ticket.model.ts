export interface ITicket {
  id?: number;
  ticketTypeId?: number;
  guestId?: number;
}

export class Ticket implements ITicket {
  constructor(public id?: number, public ticketTypeId?: number, public guestId?: number) {}
}
