import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { Event } from '../model/event';

const BASE_URL = '/api/v1/events/';

@Injectable({ providedIn: 'root' })
export class EventService {

    constructor(private httpClient: HttpClient) { }

    public findAll(page = 0, size = 1): Observable<Event[]> {
        const url = `${BASE_URL}?page=${page}&size=${size}`;
        return this.httpClient.get<any>(url).pipe(
            map(response => response.content)
        );
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

}