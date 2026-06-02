import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { Event } from '../model/event';

const BASE_URL = '/api/v1/events/';

export interface EventFilters {
    participantId?: number | string | null;
    title?: string | null;
    category?: string | null;
    minPrice?: number | null;
    maxPrice?: number | null;
    sortBy?: string | null;
    sortDir?: 'asc' | 'desc' | null;
    page?: number;
    size?: number;
}

@Injectable({ providedIn: 'root' })
export class EventService {

    constructor(private httpClient: HttpClient) { }

    public fetchEvents(filters: EventFilters = {}): Observable<Event[]> {
        let params = new HttpParams()
            .set('page', filters.page ?? 0)
            .set('size', filters.size ?? 10);

        if (filters.participantId !== undefined && filters.participantId !== null && filters.participantId !== '') {
            params = params.set('participantId', Number(filters.participantId));
        }

        if (filters.title && filters.title.trim().length > 0) {
            params = params.set('title', filters.title.trim());
        }

        if (filters.category && filters.category.trim().length > 0) {
            params = params.set('category', filters.category.trim());
        }

        if (filters.minPrice !== undefined && filters.minPrice !== null) {
            params = params.set('minPrice', filters.minPrice);
        }

        if (filters.maxPrice !== undefined && filters.maxPrice !== null) {
            params = params.set('maxPrice', filters.maxPrice);
        }

        if (filters.sortBy && filters.sortBy.trim().length > 0) {
            params = params.set('sortBy', filters.sortBy.trim());
        }

        if (filters.sortDir) {
            params = params.set('sortDir', filters.sortDir);
        }

        return this.httpClient.get<any>(BASE_URL, { params }).pipe(
            map(response => response.content)
        );
    }


    public findAll(page = 0, size = 1): Observable<Event[]> {
        return this.fetchEvents({ page, size });
    }

    public findById(eventId: number | string): Observable<Event> {
        const id = Number(eventId);
        return this.httpClient.get<Event>(BASE_URL + id);
    }

    public deleteById(eventId: number | string): Observable<Event> {
        const id = Number(eventId);
        return this.httpClient.delete<Event>(BASE_URL + id);
    }


    public createOrReplaceEvent(event: Event): Observable<Event> {
        if (event.id) {
            const id = Number(event.id);
            return this.httpClient.put<Event>(BASE_URL + id, event);
        } else {
            return this.httpClient.post<Event>(BASE_URL, event);
        }
    }

    public createOrReplaceEventImage(event: Event, image: File): Observable<Event> {

        const id = Number(event.id);
        const formData = new FormData();
        formData.append('imageFile', image);

        if (event.image) {
            return this.httpClient.put<any>(BASE_URL + id + '/image', formData);
        } else {
            return this.httpClient.post<any>(BASE_URL + id + '/image', formData);
        }
    }

    public deleteEventImage(event: Event): Observable<Event> {
        const id = Number(event.id);
        return this.httpClient.delete<any>(BASE_URL + id + '/image');
    }

    public getEventsByParticipantId(participantId: number | string, page = 0, size = 10): Observable<Event[]> {
        return this.fetchEvents({ participantId, page, size });
    }

}