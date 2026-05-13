import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../service/auth.service';
import { UserService } from '../../service/user.service';
import { User } from '../../model/user';

const mockUser: User = {
  id: 1,
  fullname: 'Test User',
  username: 'testuser',
  email: 'test@email.com',
  phone: 123456789,
  password: '',
  numTicketsBought: 0,
  favoriteGenre: 'None',
  roles: ['USER']
};

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: any;
  let userServiceMock: any;
  let navigateSpy: any;

  beforeEach(async () => {
    authServiceMock = {
      login: vi.fn().mockReturnValue(of({ status: 'SUCCESS' })),
      emitLoginStatus: vi.fn(),
      setCurrentUser: vi.fn()
    };

    userServiceMock = {
      getCurrentUser: vi.fn().mockReturnValue(of(mockUser))
    };

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authServiceMock },
        { provide: UserService, useValue: userServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    const router = TestBed.inject(Router);
    navigateSpy = vi.spyOn(router, 'navigate');

    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should login, set current user and navigate home', () => {
    component.username = 'testuser';
    component.password = 'password';

    component.login();

    expect(authServiceMock.login).toHaveBeenCalledWith({ username: 'testuser', password: 'password' });
    expect(authServiceMock.emitLoginStatus).toHaveBeenCalledWith(true);
    expect(userServiceMock.getCurrentUser).toHaveBeenCalled();
    expect(authServiceMock.setCurrentUser).toHaveBeenCalledWith(mockUser);
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });

  it('should navigate to login error when current user fails', () => {
    const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});
    userServiceMock.getCurrentUser = vi.fn().mockReturnValue(throwError(() => new Error('User failed')));

    component.login();

    expect(authServiceMock.emitLoginStatus).toHaveBeenCalledWith(true);
    expect(navigateSpy).toHaveBeenCalledWith(['/error/login']);
    consoleSpy.mockRestore();
  });

  it('should render login form', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Welcome Back');
    expect(compiled.textContent).toContain('Username');
    expect(compiled.textContent).toContain('Password');
    expect(compiled.textContent).toContain('Login');
  });
});
