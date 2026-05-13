import { ChangeDetectorRef } from "@angular/core";
import { Observable } from "rxjs";

export interface PaginationState {
    page: number;
    size: number;
    hasMore: boolean;
}

export function loadPaginatedItems<T>(
    state: PaginationState,
    currentItems: T[],
    findAll: (page: number, size: number) => Observable<T[]>,
    updateItems: (items: T[]) => void,
    cd?: ChangeDetectorRef
): void {
    if (!state.hasMore) {
        return;
    }

    findAll(state.page, state.size).subscribe({
        next: (items) => {
            updateItems(currentItems.concat(items));
            state.page++;
            state.hasMore = items.length === state.size;
            cd?.detectChanges();
        }
    });
}
