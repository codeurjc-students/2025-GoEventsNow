import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component } from "@angular/core";
import { User } from "../../model/user";
import { Ticket } from "../../model/ticket";
import { Event } from "../../model/event";
import { UserService } from "../../service/user.service";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { EventService } from "../../service/event.service";
import { NgbAlert } from "@ng-bootstrap/ng-bootstrap/alert";
import { getSelectedFile } from "../../utils/file-utils";
import { Participant } from "../../model/participant";

@Component({
    standalone: true,
    selector: 'app-user-page',
    templateUrl: './user-page.component.html',
    imports: [CommonModule, FormsModule, NgbAlert]
})

export class UserPageComponent {

    section: string = 'personal';
    profileImage: File | null = null;
    eventsMap: Map<number, Event> = new Map();
    user: User = {} as User;
    userId: string | null = '';
    userPhone: string = '';
    removeImage: boolean = false;
    tickets: Ticket[] = [];
    errorMessage: string | null = null;
    favoriteEvents: Event[] = [];
    followedParticipants: Participant[] = [];

    constructor(private router: Router, private eventService: EventService, private userService: UserService, private cd: ChangeDetectorRef, private activatedRoute: ActivatedRoute) {

        this.userId = this.activatedRoute.snapshot.paramMap.get('id');

        if (this.userId) {
            this.userService.getCurrentUser().subscribe({
                next: (user: User) => {

                    if (user.id !== Number(this.userId)) {
                        this.router.navigate(['/error/unauthorized']);
                        return;
                    }

                    this.user = user;
                    this.tickets = this.user.tickets || [];
                    this.favoriteEvents = this.user.favoriteEvents || [];
                    this.followedParticipants = user.followedParticipants || [];
                    this.loadEventsForTickets();                    
                }
            });
        }
    }

    send(): void {

        if (!this.isFormUserValid()) {
            this.errorMessage = 'Please fill out all fields correctly.';
            return;
        }

        this.errorMessage = null;

        this.userService.replaceUser(this.user).subscribe({
            next: (user: User) => {
                if (this.removeImage) {
                    this.userService.deleteUserImage(user).subscribe({
                        next: () => { this.router.navigate(['']); },
                        error: (error) => console.error('Failed to delete image:', error)
                    });
                } else if (this.profileImage) {
                    this.uploadImage(user);
                }
                this.router.navigate(['']);
            },
            error: (error) => console.error('Failed to create user:', error)
        });
    }


    uploadImage(user: User): void {
        if (this.profileImage) {
            this.userService.createOrReplaceUserImage(user, this.profileImage).subscribe({
                next: () => {
                    this.router.navigate(['']);
                },
                error: (error) => console.error('Failed to upload image:', error)
            });
        }
    }

    onFileSelected(event: any): void {
        this.profileImage = getSelectedFile(event);
    }

    loadEventsForTickets(): void {

        this.tickets.forEach(ticket => {
            this.eventService.findById(ticket.eventId).subscribe({
                next: (event) => {
                    this.eventsMap.set(ticket.eventId, event);
                }
            });
        });

        this.cd.detectChanges();

    }

    removeFavorite(id: number | undefined): void {

        if (!this.user.id) return;
        if (!id) return;

        const eventId = Number(id);
        const userId = Number(this.user.id);

        this.userService.removeFavoriteEvent(userId, eventId).subscribe({
            next: () => {
                this.favoriteEvents = this.favoriteEvents.filter(e => e.id !== eventId);
                this.cd.detectChanges();
            },
            error: (err) => {
                console.error(`Failed to remove event with id ${eventId} from favorites:`, err);
            }
        });

    }

    unfollowParticipant(id: number | undefined): void {

        if (!this.user.id) return;
        if (!id) return;

        const participantId = Number(id);
        const userId = Number(this.user.id);

        this.userService.unfollowParticipant(userId, participantId).subscribe({
            next: () => {
                this.followedParticipants = this.followedParticipants.filter(p => p.id !== participantId);
                this.cd.detectChanges();
            },
            error: (err) => {
                console.error(`Failed to unfollow participant with id ${participantId}:`, err);
            }
        });

    }

    isFormUserValid(): boolean {
        const hasValidPhone =
            this.user.phone !== null &&
            this.user.phone !== undefined &&
            /^\d{9}$/.test(this.user.phone.toString());

        return this.user.fullname.trim().length > 0 &&
            this.user.email.trim().length > 0 &&
            this.user.email.includes('@') &&
            this.user.email.includes('.') &&
            !(this.profileImage !== null && this.removeImage === true) &&
            hasValidPhone;
    }
}