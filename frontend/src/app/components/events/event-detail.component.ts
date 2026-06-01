import { ChangeDetectorRef, Component, inject, OnInit, TemplateRef } from "@angular/core";
import { Observable } from "rxjs";
import { EventService } from "../../service/event.service";
import { CommonModule } from '@angular/common';
import { FormsModule } from "@angular/forms";
import { Event } from "../../model/event";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { Review } from "../../model/review";
import { ReviewService } from "../../service/review.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap/modal";
import { NgbRating } from "@ng-bootstrap/ng-bootstrap";
import { User } from "../../model/user";
import { UserService } from "../../service/user.service";

@Component({
    standalone: true,
    selector: 'app-event-detail',
    templateUrl: './event-detail.component.html',
    styleUrl: '../user/user-page.component.css',
    imports: [CommonModule, RouterLink, FormsModule, NgbRating]
})

export class EventDetailComponent implements OnInit {

    event$: Observable<Event> = new Observable<Event>();
    participants: any[] = [];
    reviews: Review[] = [];
    currentUser: User | null = null;
    isFavorite: boolean = false;
    reviewsMap: Map<number, User> = new Map();
    activeReview: Review = {} as Review;
    modalService = inject(NgbModal);
    currentEventId: number | null = null;

    constructor(private activatedRoute: ActivatedRoute,private userService: UserService, private eventService: EventService, private router: Router, private reviewService: ReviewService, private changeDetectorRef: ChangeDetectorRef) { }

    ngOnInit(): void {
        const id = this.activatedRoute.snapshot.params['id'];
        this.currentEventId = id;
        this.event$ = this.eventService.findById(id);
        this.event$.subscribe({
            next: (event) => {
                this.participants = this.participants.concat(event.participants);
            },
            error: () => this.router.navigate(['/error/404'])
        });

        this.userService.getCurrentUser().subscribe({
            next: (user) => {
                this.currentUser = user;
                this.isFavorite = user.favoriteEvents?.some(e => e.id === Number(id)) || false;
                this.changeDetectorRef.detectChanges();
            },
            error: () => {
                this.currentUser = null;
                this.isFavorite = false;
            }
        });
        this.reviewService.getAllReviewsForEvent(id).subscribe({
            next: (reviews) => {
                this.reviews = Array.isArray(reviews) ? reviews : [];
                this.reviews.forEach(review => {
                    this.userService.findById(review.userOwnerId).subscribe({
                        next: (user) => {
                            this.reviewsMap.set(review.userOwnerId, user);
                            this.changeDetectorRef.detectChanges();
                        }
                    });
                });
            }
        });
    }

    openReviewModal(content: TemplateRef<any>, eventId?: number, review?: Review) {

        if (!review || !review.id) {
            this.activeReview = {
                description: '',
                rating: 0,
                eventAssociatedId: eventId,
                userOwnerId: Number(this.currentUser?.id)
            } as Review;
        } else {
            this.activeReview = { ...review };
        }

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
                if (this.activeReview && this.activeReview.id) {
                    const index = this.reviews.findIndex(r => r.id === updatedReview.id);
                    if (index !== -1) {
                        this.reviews[index] = updatedReview;
                    }
                } else {
                    this.reviews = [updatedReview, ...this.reviews];
                }
                this.reviewsMap.set(updatedReview.userOwnerId, this.currentUser!);
                this.changeDetectorRef.detectChanges();
            },
            error: (err) => {
                console.error('Failed to save review:', err);
            }
        });
    }

    toggleFavorite(): void {
        if (!this.currentUser || !this.currentUser.id || this.currentEventId === null) {
            return;
        }

        const userId = Number(this.currentUser.id);
        const eventId = Number(this.currentEventId);

        if (this.isFavorite) {
            this.userService.removeFavoriteEvent(userId, eventId).subscribe({
                next: () => {
                    this.isFavorite = false;
                    this.changeDetectorRef.detectChanges();
                },
                error: (error) => console.error('Failed to remove favorite:', error)
            });
        } else {
            this.userService.addFavoriteEvent(userId, eventId).subscribe({
                next: () => {
                    this.isFavorite = true;
                    this.changeDetectorRef.detectChanges();
                },
                error: (error) => console.error('Failed to add favorite:', error)
            });
        }
    }

}