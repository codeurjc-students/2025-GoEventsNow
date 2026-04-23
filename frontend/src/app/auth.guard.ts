import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './service/auth.service';
import { UserService } from './service/user.service';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';


@Injectable({providedIn: 'root'})
export class authGuard implements CanActivate {

    constructor(private authService: AuthService,private userService: UserService,private router: Router) {}

    canActivate(): Observable<boolean> {
        return this.userService.getCurrentUser().pipe(
            tap((user) => {
                this.authService.emitLoginStatus(true);
                this.authService.setCurrentUser(user);
            }),
            map(() => true),
            catchError(() => {
                this.authService.emitLoginStatus(false);
                this.authService.setCurrentUser(null);
                this.router.navigate(['/login']);
                return of(false);
            })
        );
    }
}