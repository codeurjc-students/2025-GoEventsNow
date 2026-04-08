import { Component, ChangeDetectorRef } from "@angular/core";
import { EventService } from "../../service/event.service";
import { ParticipantService } from "../../service/participant.service";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { Event } from "../../model/event";
import { Participant } from "../../model/participant";
import { Router, ActivatedRoute } from "@angular/router";
import { NgbAlert } from "@ng-bootstrap/ng-bootstrap/alert";


@Component({
    standalone: true,
    selector: 'app-add-event',
    templateUrl: './add-event.component.html',
    imports: [CommonModule, FormsModule, NgbAlert]
})

export class AddEventComponent {

    newEvent: boolean = true;
    event: Event = {
        title: '', description:'', category: '', location: '', date: '', time: '', basicPrice: 0, vipPrice: 0, availableBasicTickets: 0, availableVipTickets: 0,
        image: false, participants: [], tickets: []
    };
    allParticipants: Participant[] = [];
    selectedParticipants: number[] = [];
    imageFile: File | null = null;
    errorMessage: string | null = null;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private eventService: EventService, private participantService: ParticipantService, private cd: ChangeDetectorRef) {
        
        const eventId = this.activatedRoute.snapshot.params['id'];

        this.participantService.findAll(0, 100).subscribe({
            next: (participants) => {
                this.allParticipants = participants;
                this.cd.detectChanges();
            }
        });
        if (eventId) {
            this.newEvent = false;
            this.eventService.findById(eventId).subscribe({
                next: (event) => {
                    this.event = event;
                    this.selectedParticipants = (this.event.participants ?? []).map((participant) => participant.id!).filter((id): id is number => id !== undefined);
                    this.cd.detectChanges();
                }
            });
        }

    }


    send(): void {

        
        if (!this.isFormEventValid()) {
            this.errorMessage = 'Please fill out all fields correctly.';
            return;
        }

        this.errorMessage = null;

        this.event.participants = this.allParticipants.filter(p => p.id !== undefined && this.selectedParticipants.includes(p.id));

        this.eventService.createOrReplaceEvent(this.event).subscribe({
            next: (event: Event) => {
                console.log('Event created successfully:', event);
                if (this.imageFile) {
                    this.uploadImage(event);
                }
                this.router.navigate(['']);
            },
            error: (error) => console.error('Failed to create event:', error)
        });
    }


    uploadImage(event: Event): void {
        if (this.imageFile) {
            this.eventService.createOrReplaceEventImage(event, this.imageFile).subscribe({
                next: (event: Event) => {
                    console.log('Image uploaded successfully for event:', event);
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

    isFormEventValid(): boolean {
    return !!(
        this.event.title && this.event.title.trim().length > 0 && 
        this.event.description && this.event.description.trim().length > 0 &&
        this.event.category && this.event.category.trim().length > 0 &&
        this.event.location && this.event.location.trim().length > 0 &&
        this.event.date && this.event.date.trim().length > 0 &&
        this.event.time && this.event.time.trim().length > 0 &&
        this.event.basicPrice != null && this.event.basicPrice >= 0 &&
        this.event.vipPrice != null && this.event.vipPrice >= 0 &&
        this.event.availableBasicTickets != null && this.event.availableBasicTickets >= 0 &&
        this.event.availableVipTickets != null && this.event.availableVipTickets >= 0 &&
        this.selectedParticipants && this.selectedParticipants.length > 0
    );
}

}