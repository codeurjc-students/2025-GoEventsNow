import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { EventService } from "../../service/event.service";
import { RouterLink } from "@angular/router";
import { loadPaginatedItems } from "../../utils/pagination-utils";


@Component({
    standalone: true,
    selector: 'app-all-events',
    imports: [CommonModule,RouterLink],
    templateUrl: './all-events.component.html',
})

export class AllEventsComponent implements OnInit {

    page: number = 0;
    size: number = 10;
    events: any[] = [];
    hasMore: boolean = true;

    constructor (private eventService: EventService, private cd: ChangeDetectorRef) {}


    ngOnInit(): void {
        this.loadEvents();
    }

    loadEvents(): void {
        loadPaginatedItems(
            this,
            this.events,
            (page, size) => this.eventService.findAll(page, size),
            (events) => this.events = events,
            this.cd
        );
    }

}
