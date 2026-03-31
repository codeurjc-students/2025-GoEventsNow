import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { User } from "../model/user";
import { Observable } from "rxjs/internal/Observable";

const BASE_URL = '/api/v1/users/';

@Injectable({ providedIn: 'root' })
export class UserService {

    constructor(private httpClient: HttpClient) { }


    getCurrentUser() {
        return this.httpClient.get<User>(BASE_URL + 'me', { withCredentials: true });
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


}