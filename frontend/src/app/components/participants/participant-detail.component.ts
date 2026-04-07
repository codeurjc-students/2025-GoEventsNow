import { Component, OnInit } from "@angular/core";
import { Observable } from "rxjs/internal/Observable";
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from "@angular/router";
import { Participant } from "../../model/participant";
import { ParticipantService } from "../../service/participant.service";

@Component({
    standalone: true,
    selector: 'app-participant-detail',
    templateUrl: './participant-detail.component.html',
    imports: [CommonModule]
})

export class ParticipantDetailComponent implements OnInit {

    participant$: Observable<Participant> = new Observable<Participant>;

    constructor( private activatedRoute: ActivatedRoute, private participantService: ParticipantService) { }

    ngOnInit(): void {
        const id = this.activatedRoute.snapshot.params['id'];
        this.participant$ = this.participantService.findById(id);
    }
}