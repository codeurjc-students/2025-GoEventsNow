import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { Participant } from "../model/participant";

const BASE_URL = '/api/v1/participants/';

export interface ParticipantFilters {
    page?: number;
    size?: number;
    name?: string;
    types?: string[];
    sortBy?: string;
    sortDir?: 'asc' | 'desc';
}

@Injectable({ providedIn: 'root' })
export class ParticipantService {

    constructor(private readonly httpClient: HttpClient) { }

    public fetchParticipants(options: ParticipantFilters = {}): Observable<Participant[]> {
        let params = new HttpParams()
            .set('page', options.page ?? 0)
            .set('size', options.size ?? 10);

        if (options.name) {
            params = params.set('name', options.name);
        }

        if (options.types && options.types.length > 0) {
            options.types.forEach(type => {
                params = params.append('types', type);
            });
        }

        if (options.sortBy) {
            params = params.set('sortBy', options.sortBy);
        }

        if (options.sortDir) {
            params = params.set('sortDir', options.sortDir);
        }

        return this.httpClient.get<any>(BASE_URL, { params }).pipe(
            map(response => response.content)
        );
    }

    public findAll(page = 0, size = 1): Observable<Participant[]> {
        const url = `${BASE_URL}?page=${page}&size=${size}`;
        return this.httpClient.get<any>(url).pipe(
            map(response => response.content)
        );
    }

    public findById(participantId: number | string): Observable<Participant> {
        const id = Number(participantId);
        return this.httpClient.get<Participant>(BASE_URL + id);
    }

    public deleteById(participantId: number | string): Observable<Participant> {
        const id = Number(participantId);
        return this.httpClient.delete<Participant>(BASE_URL + id);
    }

    public createOrReplaceParticipant(participant: Participant): Observable<Participant> {
        if (participant.id) {
            const id = Number(participant.id);
            return this.httpClient.put<Participant>(BASE_URL + id, participant);
        } else {
            return this.httpClient.post<Participant>(BASE_URL, participant);
        }
    }

    public createOrReplaceParticipantImage(participant: Participant, image: File): Observable<Participant> {

        const id = Number(participant.id);
        const formData = new FormData();
        formData.append('imageFile', image);

        if (participant.participantImage) {
            return this.httpClient.put<any>(BASE_URL + id + '/image', formData);
        } else {
            return this.httpClient.post<any>(BASE_URL + id + '/image', formData);
        }
    }

    public deleteParticipantImage(participant: Participant): Observable<Participant> {
        const id = Number(participant.id);
        return this.httpClient.delete<any>(BASE_URL + id + '/image');
    }
}