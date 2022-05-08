import { FormGroup } from '@angular/forms';

export default class FormUtils {
  public static fieldInvalid(fg: FormGroup, fieldName: string): boolean {
    return fg.get(fieldName)!.invalid && (fg.get(fieldName)!.dirty || fg.get(fieldName)!.touched);
  }
}
