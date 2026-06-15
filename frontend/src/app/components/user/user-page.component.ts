import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, inject, TemplateRef } from "@angular/core";
import { User } from "../../model/user";
import { Ticket } from "../../model/ticket";
import { Event } from "../../model/event";
import { UserService } from "../../service/user.service";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { EventService } from "../../service/event.service";
import { NgbAlert } from "@ng-bootstrap/ng-bootstrap/alert";
import { getSelectedFile } from "../../utils/file-utils";
import { ReviewService } from "../../service/review.service";
import { Review } from "../../model/review";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap/modal";
import { NgbRating } from "@ng-bootstrap/ng-bootstrap";
import { Participant } from "../../model/participant";

@Component({
    standalone: true,
    selector: 'app-user-page',
    templateUrl: './user-page.component.html',
    styleUrl: './user-page.component.css',
    imports: [CommonModule, FormsModule, NgbAlert, NgbRating]
})

export class UserPageComponent {

    section: string = 'personal';
    profileImage: File | null = null;
    eventsMap: Map<number, Event> = new Map();
    reviewsMap: Map<number, Event> = new Map();
    user: User = {} as User;
    userId: string | null = '';
    userPhone: string = '';
    removeImage: boolean = false;
    tickets: Ticket[] = [];
    reviews: Review[] = [];
    errorMessage: string | null = null;
    editingReviewId: number | null = null;
    modalService = inject(NgbModal);
    activeReview: Review = {} as Review;
    favoriteEvents: Event[] = [];
    followedParticipants: Participant[] = [];

    constructor(private readonly router: Router, private readonly eventService: EventService, private readonly userService: UserService, private readonly reviewService: ReviewService, private readonly cd: ChangeDetectorRef, private readonly activatedRoute: ActivatedRoute) {

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
                    this.reviews = this.user.reviews || [];
                    this.favoriteEvents = this.user.favoriteEvents || [];
                    this.followedParticipants = user.followedParticipants || [];
                    this.loadEventsForTickets();
                    this.loadReviewsForUser();
                    
                }
            });
        }
    }

    getRoundedRating(review: Review): number {
        if (review?.rating == null) return 0;
        return Math.round(review.rating);
    }

    getStarType(review: Review, index: number): 'full' | 'half' | 'empty' {
        const r = review?.rating || 0;
        if (r >= index) return 'full';
        if (r >= index - 0.5) return 'half';
        return 'empty';
    }

    getStarClass(review: Review, index: number): string {
        const type = this.getStarType(review, index);
        if (type === 'full') return 'bi-star-fill text-warning';
        if (type === 'half') return 'bi-star-half text-warning';
        return 'bi-star text-muted';
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

    loadReviewsForUser(): void {

        this.reviewService.getAllReviewsForUsername(this.user.username).subscribe({
            next: (reviews) => {
                this.reviews = Array.isArray(reviews) ? reviews : [];
                this.reviews.forEach(review => {
                    this.eventService.findById(review.eventAssociatedId).subscribe({
                        next: (event) => {
                            this.reviewsMap.set(review.eventAssociatedId, event);
                        }
                    });
                });
            },
            error: (err) => {
                console.error('Failed to load reviews:', err);
                this.reviews = [];
            }
        });

        this.cd.detectChanges();

    }

    deleteReview(review: Review): void {

        const deletedId = Number(review.id);

        this.reviewService.deleteById(deletedId).subscribe({
            next: () => {
                this.reviews = this.reviews.filter(r => r.id !== deletedId);
                this.cd.detectChanges();
            },
            error: () => {
                console.error(`Failed to delete review with id ${deletedId}. Please try again later.`);
            }
        });
    }

    openEditModal(content: TemplateRef<any>, review: Review) {
        this.activeReview = review;
        if (!review?.id) return;

        this.activeReview = { ...review };

        this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title', centered: true }).result.then(
            (result) => {
                if (result === 'Save click') {
                    this.saveReviewUpdate();
                }
            }
        );
    }

    saveReviewUpdate() {

        this.reviewService.createOrReplaceReview(this.activeReview).subscribe({
            next: (updatedReview: Review) => {
                const index = this.reviews.findIndex(r => r.id === updatedReview.id);
                if (index !== -1) {
                    this.reviews[index] = updatedReview;
                    this.cd.detectChanges();
                }
            },
            error: (err) => {
                console.error('Failed to update review:', err);
            }
        });
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