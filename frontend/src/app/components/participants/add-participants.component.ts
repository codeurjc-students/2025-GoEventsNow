import { Component, ChangeDetectorRef } from "@angular/core";
import { ParticipantService } from "../../service/participant.service";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { Participant } from "../../model/participant";
import { Router, ActivatedRoute } from "@angular/router";
import { NgbAlert } from "@ng-bootstrap/ng-bootstrap/alert";


@Component({
    standalone: true,
    selector: 'app-add-participants',
    templateUrl: './add-participants.component.html',
    imports: [CommonModule, FormsModule, NgbAlert]
})

export class AddParticipantsComponent {

    newParticipant: boolean = true;
    participant: Participant = {
        name: '', type: '', biography: '', participantImage: false
    };
    errorMessage: string | null = null;
    imageFile: File | null = null;

    constructor(private activatedRoute: ActivatedRoute, private router: Router,private participantService: ParticipantService, private cd: ChangeDetectorRef) {
        
        const participantId = this.activatedRoute.snapshot.params['id'];

        if (participantId) {
            this.newParticipant = false;
            this.participantService.findById(participantId).subscribe({
                next: (participant) => {
                    this.participant = participant;
                    this.cd.detectChanges();
                }
            });
        }

    }


    send(): void {

        if (!this.isFormParticipantValid()) {
            this.errorMessage = 'Please fill out all fields correctly.';
            return;
        }

        this.errorMessage = null;

        this.participantService.createOrReplaceParticipant(this.participant).subscribe({
            next: (participant: Participant) => {
                if (this.imageFile) {
                    this.uploadImage(participant);
                }
                this.router.navigate(['']);
            },
            error: (error) => console.error('Failed to create participant:', error)
        });
    }


    uploadImage(participant: Participant): void {
        if (this.imageFile) {
            this.participantService.createOrReplaceParticipantImage(participant, this.imageFile).subscribe({
                next: (participant: Participant) => {
                    console.log('Image uploaded successfully for participant:', participant);
                    this.router.navigate(['']);
                },
                error: (error) => console.error('Failed to upload image:', error)
            });
        }
    }

    onFileSelected(event: any): void {
        const file = event.target.files[0];
        this.imageFile = file ? file : null;
    }

    isFormParticipantValid(): boolean {
        return this.participant.name.trim().length > 0 &&
            this.participant.type.trim().length > 0 &&
            this.participant.biography.trim().length > 0;
    }

}