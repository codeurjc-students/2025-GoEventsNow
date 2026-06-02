
import { Component, OnInit } from '@angular/core'
import { EventService } from '../../service/event.service';
import { Event } from '../../model/event';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { RouterLink } from '@angular/router';
import { Participant } from '../../model/participant';
import { ParticipantService } from '../../service/participant.service';

@Component ({
    standalone: true,
    selector: 'app-event-list',
    templateUrl: './event-list.component.html',
    imports: [CommonModule,  RouterLink]
})

export class EventListComponent implements OnInit {


    events$: Observable<Event[]> = new Observable<Event[]> ();
    participants$: Observable<Participant[]> = new Observable<Participant[]>();
    loading: boolean = false;
    page: number = 0;
    size: number = 3;

    constructor (private eventService:EventService, private participantService:ParticipantService){}

    ngOnInit():void{
        this.events$ = this.eventService.fetchEvents({ page: this.page, size: this.size });
        this.participants$ = this.participantService.fetchParticipants({ page: this.page, size: this.size });
    }

}