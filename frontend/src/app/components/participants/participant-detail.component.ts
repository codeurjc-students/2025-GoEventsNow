import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { Observable } from "rxjs/internal/Observable";
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from "@angular/router";
import { Participant } from "../../model/participant";
import { ParticipantService } from "../../service/participant.service";
import { EventService } from "../../service/event.service";

@Component({
    standalone: true,
    selector: 'app-participant-detail',
    templateUrl: './participant-detail.component.html',
    imports: [CommonModule]
})

export class ParticipantDetailComponent implements OnInit {

    participant$: Observable<Participant> = new Observable<Participant>;
    events: any[] = [];

    constructor( private activatedRoute: ActivatedRoute, private participantService: ParticipantService, private eventService:EventService,private changeDetectorRef: ChangeDetectorRef) { }

    ngOnInit(): void {
        const id = this.activatedRoute.snapshot.params['id'];
        this.participant$ = this.participantService.findById(id);
        this.eventService.getEventsByParticipantId(id).subscribe({
        next: (events) => {
            this.events = events; 
            this.changeDetectorRef.detectChanges();
        },
        error: (err) => console.error(err)
    });
    }
}