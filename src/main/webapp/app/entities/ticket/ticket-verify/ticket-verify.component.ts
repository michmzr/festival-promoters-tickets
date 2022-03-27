import { Component, OnInit } from '@angular/core';
import { JhiDataUtils } from 'ng-jhipster';
import { ActivatedRoute } from '@angular/router';
import { DomSanitizer } from '@angular/platform-browser';
import { TicketVerifyService } from 'app/entities/ticket/ticket-verify/ticket-verify.service';
import { ITicketVerificationStatus, VerificationStatus } from 'app/shared/model/ticket.model';

@Component({
  selector: 'jhi-ticket-verify',
  templateUrl: './ticket-verify.component.html',
  styleUrls: ['./ticket-verify.component.scss'],
})
export class TicketVerifyComponent implements OnInit {
  uuid: String | null = null;
  checking: Boolean = true;
  status: VerificationStatus | null = null;
  verificationResult?: ITicketVerificationStatus | null = null;

  constructor(
    protected ticketVerifyService: TicketVerifyService,
    protected dataUtils: JhiDataUtils,
    protected route: ActivatedRoute,
    private _sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.uuid = this.route.snapshot.params.uuid;
    this.checking = true;

    if (this.uuid) {
      console.info(`Checking ticket ${this.uuid}....`);

      this.ticketVerifyService.find(this.uuid).subscribe(
        r => {
          console.info(`Loaded ${r.body}`);
          this.checking = false;
          this.verificationResult = r.body;
          this.status = this.verificationResult!.status;
        },
        error => {
          this.checking = false;
          console.error('Error: ', error);
        }
      );
    }
  }

  jumboStyle(): string {
    if (this.verificationResult) {
      switch (VerificationStatus[this.verificationResult.status]) {
        case VerificationStatus.OK:
          return 'jumbo-bg-ok';
        case VerificationStatus.ALREADY_VALIDATED:
          return 'jumbo-bg-already-validated';
        case VerificationStatus.DEACTIVATED:
          return 'jumbo-bg-error';
        case VerificationStatus.NOT_FOUND:
          return 'jumbo-bg-error';
        default:
          return 'bg-primary';
      }
    } else {
      return 'bg-primary';
    }
  }
}
