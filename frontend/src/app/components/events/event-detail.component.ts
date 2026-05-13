import { Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { EventService } from "../../service/event.service";
import { CommonModule } from '@angular/common';
import { Event } from "../../model/event";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";

@Component({
    standalone: true,
    selector: 'app-event-detail',
    templateUrl: './event-detail.component.html',
    imports: [CommonModule, RouterLink]
})

export class EventDetailComponent implements OnInit {

    event$: Observable<Event> = new Observable<Event>();
    participants: any[] = [];

    constructor(private activatedRoute: ActivatedRoute, private eventService: EventService, private router: Router) { }

    ngOnInit(): void {
        const id = this.activatedRoute.snapshot.params['id'];
        this.event$ = this.eventService.findById(id);
        this.event$.subscribe({
            next: (event) => {
                this.participants = this.participants.concat(event.participants);
            },
            error: () => this.router.navigate(['/error/404'])
        });
    }
}