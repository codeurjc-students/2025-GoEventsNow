import { Component } from "@angular/core";
import { RouterLink } from "@angular/router";
import { NgbAccordionModule } from '@ng-bootstrap/ng-bootstrap';


@Component({
    standalone: true,
    selector: 'app-help',
    templateUrl: './help.component.html',
    imports: [RouterLink, NgbAccordionModule]
})

export class HelpComponent {

}