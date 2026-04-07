import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { EventService } from "../../service/event.service";
import { RouterLink } from "@angular/router";


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

        if (!this.hasMore) {
            return;
        }

        this.eventService.findAll(this.page, this.size).subscribe({
            next: (events) => {
                if (events.length < this.size) {
                    this.hasMore = false;
                }
    
                this.events = this.events.concat(events);
                this.page ++;
                this.hasMore = events.length === this.size;
                this.cd.detectChanges();
            }
        });
    }

}
