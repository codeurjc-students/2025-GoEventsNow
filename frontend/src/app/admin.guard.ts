import { Injectable } from "@angular/core";
import { CanActivate, Router } from "@angular/router";
import { UserService } from "./service/user.service";
import { AuthService } from "./service/auth.service";
import { User } from "./model/user";
import { Observable, of } from "rxjs";
import { catchError, map, tap } from "rxjs/operators";


@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {

    constructor(private readonly authService: AuthService, private readonly userService: UserService, private readonly router: Router) { }

    canActivate(): Observable<boolean> {
        return this.userService.getCurrentUser().pipe(
            tap((user: User) => {
                this.authService.emitLoginStatus(true);
                this.authService.setCurrentUser(user);
            }),
            map((user: User) => {
                if (user.roles?.includes('ADMIN')) {
                    return true;
                }
                this.router.navigate(['/error/unauthorized']);
                return false;
            }),
            catchError(() => {
                this.authService.emitLoginStatus(false);
                this.authService.setCurrentUser(null);
                this.router.navigate(['/login']);
                return of(false);
            })
        );
    }
}