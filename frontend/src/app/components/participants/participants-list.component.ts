import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { RouterLink } from "@angular/router";
import { ParticipantService } from "../../service/participant.service";
import { loadPaginatedItems } from "../../utils/pagination-utils";


@Component({
    standalone: true,
    selector: 'app-participants-list',
    imports: [CommonModule,RouterLink],
    templateUrl: './participants-list.component.html',
})

export class ParticipantsListComponent implements OnInit {

    page: number = 0;
    size: number = 10;
    participants: any[] = [];
    hasMore: boolean = true;

    constructor (private participantService: ParticipantService, private cd: ChangeDetectorRef) {}


    ngOnInit(): void {
        this.loadParticipants();
    }

    loadParticipants(): void {
        loadPaginatedItems(
            this,
            this.participants,
            (page, size) => this.participantService.findAll(page, size),
            (participants) => this.participants = participants,
            this.cd
        );
    }

}
