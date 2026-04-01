import { ChangeDetectorRef, Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { AuthService } from "../../service/auth.service";
import { Router, RouterLink } from "@angular/router";
import { CommonModule } from "@angular/common";
import { NgbAlertModule } from "@ng-bootstrap/ng-bootstrap/alert";

@Component({
    standalone: true,
    selector: 'app-register',
    imports: [CommonModule, FormsModule, NgbAlertModule, RouterLink],
    templateUrl: './register.component.html'
})
export class RegisterComponent {

    fullname: string = '';
    username: string = '';
    email: string = '';
    phone: string = '';
    password: string = '';
    confirmPassword: string = '';
    profilePicture: File | null = null;
    errorMessage: string | null = null;

    constructor(private authService: AuthService, private router: Router, private changeDetectorRef: ChangeDetectorRef) { }


    register(): void {

        if (!this.isFormValid()) {
            this.errorMessage = 'Please fill out all fields correctly.';
            return;
        }

        const formData = new FormData();
        this.errorMessage = null;

        formData.append('fullname', this.fullname);
        formData.append('username', this.username);
        formData.append('email', this.email);
        formData.append('phone', this.phone);
        formData.append('password', this.password);

        if (this.profilePicture) {
            formData.append('profileImageFile', this.profilePicture, this.profilePicture.name);
        }

        this.authService.register(formData).subscribe({
            next: () => {
                this.router.navigate(['/login']);
            },
            error: (error) => {
                this.errorMessage = error?.error?.message || 'Registration failed. Please try again.';
                this.changeDetectorRef.detectChanges();
                console.error('Registration failed:', error);
            }
        });

    }

    isFormValid(): boolean {
        return this.fullname.trim().length > 0 &&
            this.username.trim().length >= 3 &&
            this.email.trim().length > 0 && this.email.includes('@') && this.email.includes('.') &&
            this.phone.trim().length === 9 &&
            this.password.trim().length >= 5 &&
            this.password === this.confirmPassword;
    }

    onFileSelected(event: any): void {
        const file = event.target.files[0];
        this.profilePicture = file ? file : null;
    }
}