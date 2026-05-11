import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { EventService } from "../../service/event.service";
import { RouterLink,Router } from "@angular/router";
import { loadPaginatedItems } from "../../utils/pagination-utils";


@Component({
    standalone: true,
    selector: 'app-manage-events',
    imports: [CommonModule,RouterLink],
    templateUrl: './manage-events.component.html',
})

export class ManageEventsComponent implements OnInit {

    page: number = 0;
    size: number = 10;
    events: any[] = [];
    hasMore: boolean = true;

    constructor (private router:Router,private eventService: EventService, private cd: ChangeDetectorRef) {}


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

    deleteEvent(id: number): void {
        this.eventService.deleteById(id).subscribe({
            next: () => {
                console.log(`Event with id ${id} deleted successfully.`);
                this.events = this.events.filter(e => e.id !== id);
                this.cd.detectChanges();
                this.router.navigate(['/manage-events']);
            },
            error: () => {alert(`Failed to delete event with id ${id}. Please try again later.`);}
        });
    }

    editEvent(id: number): void {
        this.router.navigate(['/edit-event/' + id]);
    }

}
