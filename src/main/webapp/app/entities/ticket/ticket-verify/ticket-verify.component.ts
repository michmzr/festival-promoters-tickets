import { Component, OnInit } from '@angular/core';
import { JhiDataUtils } from 'ng-jhipster';
import { ActivatedRoute } from '@angular/router';
import { DomSanitizer } from '@angular/platform-browser';
import { TicketVerifyService } from 'app/entities/ticket/ticket-verify/ticket-verify.service';
import { ITicketVerificationStatus } from 'app/shared/model/ticket.model';

@Component({
  selector: 'jhi-ticket-verify',
  templateUrl: './ticket-verify.component.html',
  styleUrls: ['./ticket-verify.component.scss'],
})
export class TicketVerifyComponent implements OnInit {
  uuid: String | null = null;
  verificationResult: ITicketVerificationStatus | null = null;

  constructor(
    protected ticketVerifyService: TicketVerifyService,
    protected dataUtils: JhiDataUtils,
    protected route: ActivatedRoute,
    private _sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.uuid = this.route.snapshot.params.uuid;

    if (this.uuid) {
      console.info('Checking ticket ' + this.uuid);
      this.ticketVerifyService.find(this.uuid).subscribe(
        r => {
          this.verificationResult = r.body;
        },
        error => {
          if (error.status === 422) this.verificationResult = error.body;
          //todo
          else console.error('Error: ', error);
        }
      );
    }
  }
}
