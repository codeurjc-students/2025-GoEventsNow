import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';
import { AuthService } from '../../service/auth.service';
import { Router, provideRouter } from '@angular/router';
import { BehaviorSubject, of } from 'rxjs';
import { User } from '../../model/user';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let authServiceMock: any;
  let loginStatus$: BehaviorSubject<boolean>;
  let currentUser$: BehaviorSubject<User | null>;
  let navigateSpy: any;

  const adminUser: User = {
    id: 1,
    username: 'admin',
    fullname: 'Admin User',
    email: 'admin@email.com',
    phone: 123456789,
    roles: ['ADMIN']
  } as User;

  const normalUser: User = {
    id: 2,
    username: 'user',
    fullname: 'Normal User',
    email: 'user@email.com',
    phone: 987654321,
    roles: ['USER']
  } as User;

  beforeEach(async () => {
    loginStatus$ = new BehaviorSubject<boolean>(false);
    currentUser$ = new BehaviorSubject<User | null>(null);

    authServiceMock = {
      getLoginStatus: vi.fn().mockReturnValue(loginStatus$.asObservable()),
      getCurrentUser: vi.fn().mockReturnValue(currentUser$.asObservable()),
      emitLoginStatus: vi.fn(),
      setCurrentUser: vi.fn(),
      logout: vi.fn().mockReturnValue(of({}))
    };

    await TestBed.configureTestingModule({
      imports: [HeaderComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;

    const router = TestBed.inject(Router);
    navigateSpy = vi.spyOn(router, 'navigate');

    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should start as not logged', () => {
    expect(component.isLogged).toBe(false);
    expect(component.user).toBeNull();
    expect(component.admin).toBe(false);
  });

  it('should update login status', () => {
    loginStatus$.next(true);
    fixture.detectChanges();

    expect(component.isLogged).toBe(true);
  });

  it('should set admin true when current user has ADMIN role', () => {
    currentUser$.next(adminUser);
    fixture.detectChanges();

    expect(component.user?.username).toBe('admin');
    expect(component.admin).toBe(true);
  });

  it('should set admin false when current user has USER role', () => {
    currentUser$.next(normalUser);
    fixture.detectChanges();

    expect(component.user?.username).toBe('user');
    expect(component.admin).toBe(false);
  });

  it('should render login and register buttons when not logged', () => {
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Log In');
    expect(compiled.textContent).toContain('Sign Up');
  });

  it('should render username when logged', () => {
    loginStatus$.next(true);
    currentUser$.next(adminUser);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('admin');
  });

  it('should render admin options when user is admin', () => {
    loginStatus$.next(true);
    currentUser$.next(adminUser);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Manage Events');
    expect(compiled.textContent).toContain('Manage Participants');
  });

  it('should not render admin options when user is not admin', () => {
    loginStatus$.next(true);
    currentUser$.next(normalUser);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).not.toContain('Manage Events');
    expect(compiled.textContent).not.toContain('Manage Participants');
  });

  it('should logout and navigate home', () => {
    component.logout();

    expect(authServiceMock.emitLoginStatus).toHaveBeenCalledWith(false);
    expect(authServiceMock.setCurrentUser).toHaveBeenCalledWith(null);
    expect(authServiceMock.logout).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });
});