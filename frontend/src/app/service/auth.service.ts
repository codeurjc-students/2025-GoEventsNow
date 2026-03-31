
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { User } from '../model/user';

@Injectable({ providedIn: 'root' })
export class AuthService {

    private baseUrl: string = '/api/v1/auth/';
    private loggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
    private currentUser: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);

    constructor(private httpClient: HttpClient) { }

    public login (credentials: { username: string, password: string }): Observable<any> {
        return this.httpClient.post(this.baseUrl + 'login', credentials, { withCredentials: true }).pipe(
            tap((response: any) => {
                if (response.status === 'SUCCESS') {
                    this.emitLoginStatus(true);
                    this.setCurrentUser(response.user);
                }
            })
        );
    }

    public refresh (): Observable<any> {
        return this.httpClient.post(this.baseUrl + 'refresh', {}, { withCredentials: true });
    }

    public logout (): Observable<any> {
        return this.httpClient.post(this.baseUrl + 'logout', {}, { withCredentials: true });
    }

    public register (formData: FormData): Observable<any> {
        return this.httpClient.post(this.baseUrl + 'register', formData);
    }

    private hasSessionCookie(): boolean {
        return false;
    }

    public emitLoginStatus(isLoggedIn: boolean): void {
        this.loggedIn.next(isLoggedIn);
    }

    public getLoginStatus(): Observable<boolean> {
        return this.loggedIn.asObservable();
    }

    public isAuthenticated(): Observable<boolean> {
        return this.getLoginStatus();
    }

    public getCurrentUser(): Observable<User | null> {
        return this.currentUser.asObservable();
    }

    public setCurrentUser(user: User | null): void {
        this.currentUser.next(user);
    }

}