import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { RouterLink } from "@angular/router";
import { ParticipantService } from "../../service/participant.service";


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

        if (!this.hasMore) {
            return;
        }

        this.participantService.findAll(this.page, this.size).subscribe({
            next: (participants) => {
                if (participants.length < this.size) {
                    this.hasMore = false;
                }
    
                this.participants = this.participants.concat(participants);
                this.page ++;
                this.hasMore = participants.length === this.size;
                this.cd.detectChanges();
            }
        });
    }

}
