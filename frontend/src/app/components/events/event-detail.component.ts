import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { EventService } from "../../service/event.service";
import { CommonModule } from '@angular/common';
import { FormsModule } from "@angular/forms";
import { Event } from "../../model/event";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { User } from "../../model/user";
import { UserService } from "../../service/user.service";

@Component({
    standalone: true,
    selector: 'app-event-detail',
    templateUrl: './event-detail.component.html',
    imports: [CommonModule, RouterLink, FormsModule]
})

export class EventDetailComponent implements OnInit {

    event$: Observable<Event> = new Observable<Event>();
    participants: any[] = [];
    currentUser: User | null = null;
    isFavorite: boolean = false;
    currentEventId: number | null = null;

    constructor(private activatedRoute: ActivatedRoute,private userService: UserService, private eventService: EventService, private router: Router, private changeDetectorRef: ChangeDetectorRef) { }

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