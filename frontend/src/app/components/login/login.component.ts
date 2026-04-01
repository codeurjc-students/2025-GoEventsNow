import { Component } from "@angular/core";
import { AuthService } from "../../service/auth.service";
import { User } from "../../model/user";
import { Observable } from "rxjs";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { UserService } from "../../service/user.service";

@Component({
    standalone: true,
    selector: 'app-login',
    imports: [FormsModule,RouterLink],
    templateUrl: './login.component.html'
})
export class LoginComponent {

    username: string = '';
    password: string = '';

    constructor(private authService: AuthService, private userService: UserService, private router: Router) {
     }

    login(): void {
        this.authService.login({ username: this.username, password: this.password }).subscribe({
            next: () => {
                this.authService.emitLoginStatus(true);
                this.userService.getCurrentUser().subscribe({
                    next: (user) => {
                        this.authService.setCurrentUser(user);
                        this.router.navigate(['']);
                    },
                    error: (error) => {
                        console.error(error);
                        this.router.navigate(['/error/login']);
                    }
                });
            },
            error: (error) => {
                console.error(error); 
                this.router.navigate(['/error/login']);    
            }
        });
    }

}