import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { User } from "../model/user";
import { Observable } from "rxjs/internal/Observable";
import { map } from "rxjs/internal/operators/map";
import { Participant } from "../model/participant";


const BASE_URL = '/api/v1/users/';

@Injectable({ providedIn: 'root' })
export class UserService {

    constructor(private readonly httpClient: HttpClient) { }


    public getCurrentUser() {
        return this.httpClient.get<User>(BASE_URL + 'me', { withCredentials: true });
    }

    public findById(id: number): Observable<User> {
        return this.httpClient.get<User>(BASE_URL + id);
    }

    public userExists(username: string): Observable<boolean> {
        const params = `exists?username=${encodeURIComponent(username)}`;
        return this.httpClient.get<boolean>(BASE_URL + params);
    }

    public replaceUser(user: User): Observable<User> {
        const id = Number(user.id);
        return this.httpClient.put<User>(BASE_URL + id, user);

    }

    public createOrReplaceUserImage(user: User, image: File): Observable<User> {

        const id = Number(user.id);
        const formData = new FormData();
        formData.append('imageFile', image);

        if (user.profileImage) {
            return this.httpClient.put<any>(BASE_URL + id + '/image', formData);
        } else {
            return this.httpClient.post<any>(BASE_URL + id + '/image', formData);
        }
    }

    public deleteUserImage(user: User): Observable<User> {
        const id = Number(user.id);
        return this.httpClient.delete<any>(BASE_URL + id + '/image');
    }

    public addFavoriteEvent(userId: number, eventId: number): Observable<User> {
        return this.httpClient.post<User>(BASE_URL + `${userId}/favorites/${eventId}`, null);
    }

    public removeFavoriteEvent(userId: number, eventId: number): Observable<User> {
        return this.httpClient.delete<User>(BASE_URL + `${userId}/favorites/${eventId}`);
    }

    public getFavoriteEvents(userId: number, page: number, size: number): Observable<Event[]> {
        const url = `${BASE_URL}${userId}/favorites?page=${page}&size=${size}`;
        return this.httpClient.get<any>(url).pipe(
            map(response => response.content)
        );
    }

    public followParticipant(userId: number, participantId: number): Observable<User> {
        return this.httpClient.post<User>(BASE_URL + `${userId}/following/${participantId}`, null);
    }

    public unfollowParticipant(userId: number, participantId: number): Observable<User> {
        return this.httpClient.delete<User>(BASE_URL + `${userId}/following/${participantId}`);
    }

    public getFollowedParticipants(userId: number, page: number, size: number): Observable<Participant[]> {
        const url = `${BASE_URL}${userId}/following?page=${page}&size=${size}`;
        return this.httpClient.get<any>(url).pipe(
            map(response => response.content)
        );
    }

}
