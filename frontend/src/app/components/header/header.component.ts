import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterLink } from "@angular/router";
import { AuthService } from '../../service/auth.service';
import { User } from '../../model/user';
import { CommonModule } from '@angular/common';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap/dropdown';

@Component({
    standalone: true,
    selector: 'app-header',
    templateUrl: './header.component.html',
    imports: [RouterLink, CommonModule, NgbDropdownModule]
})
export class HeaderComponent implements OnInit {

    isLogged: boolean = false;
    user: User | null = null;
    admin: boolean = false;

    constructor(private readonly authService: AuthService, private readonly router: Router, private readonly changeDetectorRef: ChangeDetectorRef) { }

    ngOnInit(): void {
        this.authService.getLoginStatus().subscribe(status => {
            this.isLogged = status;
        });

        this.authService.getCurrentUser().subscribe(user => {
            this.user = user;
            if (this.user?.roles?.includes('ADMIN')) {
                this.admin = true;
            } else{
                this.admin = false;
            }
            this.changeDetectorRef.detectChanges();
        });
    }

    logout(): void {
        this.authService.emitLoginStatus(false);
        this.authService.setCurrentUser(null);
        this.authService.logout().subscribe({
            next: () => {
                this.router.navigate(['']);
            }
        });
    }
}