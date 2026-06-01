import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { RouterLink } from "@angular/router";
import { EventService } from "../../service/event.service";
import { UserService } from "../../service/user.service";
import { User } from "../../model/user";
import { Event } from "../../model/event";

@Component({
    standalone: true,
    selector: "app-all-events",
    imports: [CommonModule, RouterLink],
    templateUrl: "./all-events.component.html",
})
export class AllEventsComponent implements OnInit {

    page = 0;
    size = 10;
    events: Event[] = [];
    hasMore = true;
    loading = false;

    currentUser: User | null = null;
    favoriteEventIds = new Set<number>();
    private requestToken = 0;

    titleFilter = "";
    categoryFilter = "";
    participantIdFilter = "";
    minPriceFilter = "";
    maxPriceFilter = "";

    sortBy: string | null = null;
    sortDir: "asc" | "desc" = "desc";

    constructor(
        private eventService: EventService,
        private userService: UserService,
        private cd: ChangeDetectorRef
    ) { }

    ngOnInit(): void {
        this.loadCurrentUser();
        this.loadEvents();
    }

    loadCurrentUser(): void {
        this.userService.getCurrentUser().subscribe({
            next: user => {
                this.currentUser = user;
                this.favoriteEventIds = new Set(
                    user.favoriteEvents?.map(event => event.id!).filter(id => id !== undefined) || []
                );
                this.cd.detectChanges();
            },
            error: () => {
                this.currentUser = null;
            }
        });
    }

    loadEvents(): void {
        if (!this.hasMore || this.loading) return;

        const currentToken = this.requestToken;
        this.loading = true;

        this.eventService.fetchEvents({
            participantId: this.participantIdFilter || null,
            title: this.titleFilter || null,
            category: this.categoryFilter || null,
            minPrice: this.toNumberOrNull(this.minPriceFilter),
            maxPrice: this.toNumberOrNull(this.maxPriceFilter),
            sortBy: this.sortBy ?? undefined,
            sortDir: this.sortDir,
            page: this.page,
            size: this.size
        }).subscribe({
            next: events => {
                if (currentToken !== this.requestToken) {
                    return;
                }

                this.events = [...this.events, ...events];
                this.page++;
                this.hasMore = events.length === this.size;
                this.loading = false;
                this.cd.detectChanges();
            },
            error: () => {
                if (currentToken === this.requestToken) {
                    this.loading = false;
                }
            }
        });
    }

    onSearch(value: string): void {
        this.titleFilter = value.trim();
        this.reloadEvents();
    }

    onCategoryChange(value: string): void {
        this.categoryFilter = value;
        this.reloadEvents();
    }

    onPriceChange(field: 'min' | 'max', value: string): void {
        if (field === 'min') {
            this.minPriceFilter = value;
        } else {
            this.maxPriceFilter = value;
        }

        this.reloadEvents();
    }

    clearFilters(): void {
        this.titleFilter = "";
        this.categoryFilter = "";
        this.participantIdFilter = "";
        this.minPriceFilter = "";
        this.maxPriceFilter = "";
        this.sortBy = null;
        this.sortDir = "desc";

        this.reloadEvents();
    }

    changeSort(sortBy: string): void {
        this.sortBy = sortBy || null;
        this.reloadEvents();
    }

    toggleSortDir(): void {
        this.sortDir = this.sortDir === "asc" ? "desc" : "asc";
        this.reloadEvents();
    }

    isFavorite(eventId: number | undefined): boolean {
        return eventId !== undefined && this.favoriteEventIds.has(eventId);
    }

    toggleFavorite(eventId: number | undefined): void {
        if (!this.currentUser?.id || eventId === undefined) return;

        const userId = Number(this.currentUser.id);

        const request = this.isFavorite(eventId)
            ? this.userService.removeFavoriteEvent(userId, eventId)
            : this.userService.addFavoriteEvent(userId, eventId);

        request.subscribe({
            next: () => {
                this.updateFavoriteState(eventId);
                this.cd.detectChanges();
            },
            error: error => {
                console.error("Failed to update favorite:", error);
            }
        });
    }

    private updateFavoriteState(eventId: number): void {
        if (this.favoriteEventIds.has(eventId)) {
            this.favoriteEventIds.delete(eventId);
        } else {
            this.favoriteEventIds.add(eventId);
        }
    }

    private resetList(): void {
        this.page = 0;
        this.events = [];
        this.hasMore = true;
    }

    private reloadEvents(): void {
        this.requestToken++;
        this.loading = false;
        this.resetList();
        this.loadEvents();
    }

    private toNumberOrNull(value: string): number | null {
        return value === "" ? null : Number(value);
    }
}