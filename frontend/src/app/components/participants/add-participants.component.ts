import { Component, ChangeDetectorRef } from "@angular/core";
import { ParticipantService } from "../../service/participant.service";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { Participant } from "../../model/participant";
import { Router, ActivatedRoute } from "@angular/router";
import { NgbAlert } from "@ng-bootstrap/ng-bootstrap/alert";
import { getSelectedFile } from "../../utils/file-utils";


@Component({
    standalone: true,
    selector: 'app-add-participants',
    templateUrl: './add-participants.component.html',
    imports: [CommonModule, FormsModule, NgbAlert]
})

export class AddParticipantsComponent {

    newParticipant: boolean = true;
    participant: Participant = {
        name: '', type: '', biography: '', participantImage: false, numFollowers: 0
    };
    removeImage: boolean = false;
    errorMessage: string | null = null;
    imageFile: File | null = null;

    constructor(private  readonly activatedRoute: ActivatedRoute, private readonly router: Router,private readonly participantService: ParticipantService, private readonly cd: ChangeDetectorRef) {
        
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
                if (this.removeImage) {
                    this.participantService.deleteParticipantImage(participant).subscribe({
                        next: () => { this.router.navigate(['']); },
                        error: (error) => console.error('Failed to delete image:', error)
                    });
                }else if (this.imageFile) {
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
        this.imageFile = getSelectedFile(event);
    }

    isFormParticipantValid(): boolean {
        return this.participant.name.trim().length > 0 &&
            this.participant.type.trim().length > 0 &&
            !(this.imageFile !== null && this.removeImage === true) &&
            this.participant.biography.trim().length > 0;
    }

}