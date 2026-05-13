import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from "@angular/router";
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

    participant$: Observable<Participant> = new Observable<Participant>();
    events: any[] = [];

    constructor( private activatedRoute: ActivatedRoute, private participantService: ParticipantService, private eventService:EventService,private changeDetectorRef: ChangeDetectorRef, private router: Router) { }

    ngOnInit(): void {
        const id = this.activatedRoute.snapshot.params['id'];
        this.participant$ = this.participantService.findById(id);
        this.participant$.subscribe({
            next: () => {
                this.eventService.getEventsByParticipantId(id).subscribe({
                    next: (events) => {
                        this.events = events;
                        this.changeDetectorRef.detectChanges();
                    },
                    error: (err) => console.error(err)
                });
            },
            error: () => this.router.navigate(['/error/404'])
        });
    }
}