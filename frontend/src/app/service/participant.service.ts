import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { Participant } from "../model/participant";

const BASE_URL = '/api/v1/participants/';

@Injectable({ providedIn: 'root' })
export class ParticipantService {

    constructor(private httpClient: HttpClient) { }

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
}