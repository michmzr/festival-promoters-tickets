// import {MatButtonToggleModule} from "@angular/material/button-toggle";
// import {MatButtonModule} from "@angular/material/button";
// import {MatCheckboxModule} from "@angular/material/checkbox";
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
// import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
// import {MatSnackBarModule} from "@angular/material/snack-bar";
import { MatTooltipModule } from '@angular/material/tooltip';
// import {MatTabsModule} from "@angular/material/tabs";
// import {MatDialogModule} from "@angular/material/dialog";
// import {MatSelectModule} from "@angular/material/select";
import { MAT_CHIPS_DEFAULT_OPTIONS, MatChipsModule } from '@angular/material/chips';
// import {MatProgressBarModule} from "@angular/material/progress-bar";
// import {MatCardModule} from "@angular/material/card";
// import {MatDatepickerModule} from "@angular/material/datepicker";
// import {MatNativeDateModule} from "@angular/material/core";
// import {MatGridListModule} from "@angular/material/grid-list";
// import {MatExpansionModule} from "@angular/material/expansion";
// import {MatAutocompleteModule} from "@angular/material/autocomplete";
import { MatIconModule } from '@angular/material/icon';
// import {MatListModule} from "@angular/material/list";
// import {MatRadioModule} from "@angular/material/radio";
// import {MatMenuModule} from "@angular/material/menu";
import { NgModule } from '@angular/core';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { MatCommonModule } from '@angular/material/core';
import { MatToolbarModule } from '@angular/material/toolbar';

// import {MatExpansionPanel} from "@angular/material/expansion";

@NgModule({
  providers: [
    {
      provide: MAT_CHIPS_DEFAULT_OPTIONS,
      useValue: {
        separatorKeyCodes: [ENTER, COMMA],
      },
    },
  ],
  imports: [MatCommonModule, MatFormFieldModule, MatInputModule, MatChipsModule, MatIconModule, MatToolbarModule, MatTooltipModule],
  exports: [MatCommonModule, MatFormFieldModule, MatInputModule, MatChipsModule, MatIconModule, MatToolbarModule, MatTooltipModule],
})
export class JhMaterialModule {}
