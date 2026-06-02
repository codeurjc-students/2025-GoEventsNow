import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { RouterLink } from "@angular/router";
import { ParticipantService } from "../../service/participant.service";

@Component({
    standalone: true,
    selector: "app-participants-list",
    imports: [CommonModule, RouterLink],
    templateUrl: "./participants-list.component.html",
})
export class ParticipantsListComponent implements OnInit {

    page = 0;
    size = 10;
    participants: any[] = [];
    hasMore = true;
    loading = false;

    types: string[] = [];
    selectedTypes: string[] = [];
    categoryFilter = "";
    searchTerm = "";
    private requestToken = 0;

    sortBy: string | null = null;
    sortDir: "asc" | "desc" = "desc";

    constructor(private participantService: ParticipantService, private cd: ChangeDetectorRef) { }

    ngOnInit(): void {
        this.loadParticipants();
        this.loadTypes();
    }

    loadParticipants(): void {
        if (!this.hasMore || this.loading) {
            return;
        }

        const currentToken = this.requestToken;
        this.loading = true;

        this.participantService.fetchParticipants({
            page: this.page,
            size: this.size,
            name: this.searchTerm || undefined,
            types: this.selectedTypes,
            sortBy: this.sortBy ?? undefined,
            sortDir: this.sortDir
        }).subscribe({
            next: participants => {
                if (currentToken !== this.requestToken) {
                    return;
                }

                this.participants = [...this.participants, ...participants];
                this.page++;
                this.hasMore = participants.length === this.size;
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

    loadTypes(): void {
        this.participantService.fetchParticipants({ page: 0, size: 1000 }).subscribe(participants => {
            this.types = [...new Set(participants.map(p => p.type).filter(Boolean))].sort();
            this.cd.detectChanges();
        });
    }

    applyFilters(): void {
        this.resetList();
        this.loadParticipants();
    }

    onSearch(value: string): void {
        this.searchTerm = value.trim();
        this.applyFilters();
    }

    onCategoryChange(value: string): void {
        this.categoryFilter = value;
        this.selectedTypes = value ? [value] : [];
        this.applyFilters();
    }

    changeSort(sortBy: string): void {
        this.sortBy = sortBy || null;
        this.applyFilters();
    }

    toggleSortDir(): void {
        this.sortDir = this.sortDir === "asc" ? "desc" : "asc";
        this.applyFilters();
    }

    clearFilters(): void {
        this.searchTerm = "";
        this.categoryFilter = "";
        this.selectedTypes = [];
        this.sortBy = null;
        this.sortDir = "desc";
        this.applyFilters();
    }

    private resetList(): void {
        this.requestToken++;
        this.loading = false;
        this.participants = [];
        this.page = 0;
        this.hasMore = true;
    }
}