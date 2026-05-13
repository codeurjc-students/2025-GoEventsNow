import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { RouterLink } from "@angular/router";
import { ParticipantService } from "../../service/participant.service";
import { loadPaginatedItems } from "../../utils/pagination-utils";


@Component({
    standalone: true,
    selector: 'app-manage-participants',
    imports: [CommonModule,RouterLink],
    templateUrl: './manage-participants.component.html',
})

export class ManageParticipantsComponent implements OnInit {

    page: number = 0;
    size: number = 10;
    participants: any[] = [];
    hasMore: boolean = true;
    alertMessage: boolean = false;
    alertMessageText: string = '';

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

    deleteParticipant(id: number): void {

        this.participantService.deleteById(id).subscribe({
            next: () => {
                this.participants = this.participants.filter(p => p.id !== id);
                this.cd.detectChanges();
            },
            error: () => {
                this.alertMessage = true;
                this.participantService.findById(id).subscribe({
                    next: (participant) => {
                        this.alertMessageText = `Failed to delete participant ${participant.name}, as it is currently in an event. Please try again later.`;
                        this.cd.detectChanges();
                    },
                    error: () => {
                        this.alertMessageText = `Failed to delete participant with id ${id}. Please try again later.`;
                    }
                });
                
            }
        });
    }

}
