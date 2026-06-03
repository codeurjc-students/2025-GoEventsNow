import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './service/auth.service';
import { UserService } from './service/user.service';

@Component({
  standalone: true,
  selector: 'app-root',
  templateUrl: './app.component.html',
  imports: [ RouterOutlet, HeaderComponent, FooterComponent ]
})
export class AppComponent implements OnInit {

  constructor(
    private readonly authService: AuthService,
    private readonly userService: UserService 
  ) { }

  ngOnInit(): void {
    this.authService.refresh().subscribe({
      next: () => {
        this.authService.emitLoginStatus(true);
        this.userService.getCurrentUser().subscribe({
          next: (user) => {
            this.authService.setCurrentUser(user);
          },
          error: (err) => {
            this.clean();
          }
        });
      },
      error: () => {
        this.clean();
      }
    });
  }

  private clean(): void {
    this.authService.emitLoginStatus(false);
    this.authService.setCurrentUser(null);
  }
}