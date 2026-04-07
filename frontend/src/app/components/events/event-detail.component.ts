import { Component, OnInit } from "@angular/core";
import { Observable } from "rxjs/internal/Observable";
import { EventService } from "../../service/event.service";
import { CommonModule } from '@angular/common';
import { Event } from "../../model/event";
import { ActivatedRoute, RouterLink } from "@angular/router";

@Component({
    standalone: true,
    selector: 'app-event-detail',
    templateUrl: './event-detail.component.html',
    imports: [CommonModule, RouterLink]
})

export class EventDetailComponent implements OnInit {

    event$: Observable<Event> = new Observable<Event>;
    participants: any[] = [];

    constructor(private activatedRoute: ActivatedRoute, private eventService: EventService) { }

    ngOnInit(): void {
        const id = this.activatedRoute.snapshot.params['id'];
        this.event$ = this.eventService.findById(id);
        this.event$.subscribe(event => {
            this.participants = this.participants.concat(event.participants);
        });
    }
}