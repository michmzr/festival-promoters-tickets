import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { MatChipInputEvent } from '@angular/material/chips';
import { PromoCodeService } from '../../promo-code/promo-code.service';

@Component({
  selector: 'jhi-promotor-promo-code-form',
  templateUrl: './promotor-promo-code-form.component.html',
  styleUrls: ['./promotor-promo-code-form.component.scss'],
})
export class PromotorPromoNewCodesFormComponent implements OnInit {
  promoCodeError?: string;
  promoCodes: string[] = [];

  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;

  @Output()
  codeAdded: EventEmitter<string> = new EventEmitter<string>();
  @Output()
  codeRemoved: EventEmitter<string> = new EventEmitter<string>();

  constructor(protected promoCodeService: PromoCodeService) {}

  ngOnInit(): void {}

  add(event: MatChipInputEvent): void {
    const code = (event.value || '').trim();

    if (code) {
      if (this.promoCodes.filter(x => x.toLowerCase().includes(code.toLowerCase())).length > 0) {
        const message = `Podobny kod ${code} już został dodany`;
        this.setPromoError(message);
        return;
      }

      this.promoCodeService.findByCode(code).subscribe(
        () => {
          this.setPromoError(`Kod '${code}' jest już zarejestrowany w systemie`);
          console.info(`Code is already registered ${code}`);
        },
        error => {
          if (error.status === 404) {
            this.promoCodes.push(code);
            this.codeAdded.emit(code);
          } else {
            console.error('Error: ', error);
          }
        }
      );
    }

    // Clear the input code
    event.input.value = '';
  }

  setPromoError(error: string): void {
    this.promoCodeError = error;
  }

  remove(code: string): void {
    const index = this.promoCodes.indexOf(code);

    if (index >= 0) {
      this.promoCodes.splice(index, 1);

      this.codeRemoved.emit(code);
    }
  }
}
