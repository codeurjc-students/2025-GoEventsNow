import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../service/auth.service';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock: any;
  let navigateSpy: any;

  beforeEach(async () => {
    authServiceMock = {
      register: vi.fn().mockReturnValue(of({}))
    };

    await TestBed.configureTestingModule({
      imports: [RegisterComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;

    const router = TestBed.inject(Router);
    navigateSpy = vi.spyOn(router, 'navigate');

    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should start with empty form fields', () => {
    expect(component.fullname).toBe('');
    expect(component.username).toBe('');
    expect(component.email).toBe('');
    expect(component.phone).toBe('');
    expect(component.password).toBe('');
    expect(component.confirmPassword).toBe('');
  });

  it('should validate a correct register form', () => {
    fillValidForm(component);

    expect(component.isFormValid()).toBe(true);
  });

  it('should invalidate form when passwords do not match', () => {
    fillValidForm(component);
    component.confirmPassword = 'different';

    expect(component.isFormValid()).toBe(false);
  });

  it('should not register when form is invalid', () => {
    component.register();

    expect(component.errorMessage).toBe('Please fill out all fields correctly.');
    expect(authServiceMock.register).not.toHaveBeenCalled();
  });

  it('should register and navigate to login when form is valid', () => {
    fillValidForm(component);

    component.register();

    expect(authServiceMock.register).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
  });

  it('should set profilePicture on file selected', () => {
    const file = new File(['fake-image'], 'noImage.png', { type: 'image/png' });

    component.onFileSelected({
      target: {
        files: [file]
      }
    });

    expect(component.profilePicture).toBe(file);
  });

  it('should show backend error message when register fails', () => {
    const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    authServiceMock.register = vi.fn().mockReturnValue(
      throwError(() => ({
        error: {
          message: 'Username already exists'
        }
      }))
    );
    fillValidForm(component);

    component.register();

    expect(component.errorMessage).toBe('Username already exists');
    consoleSpy.mockRestore();
  });

  it('should render register form', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Create an Account');
    expect(compiled.textContent).toContain('Fullname');
    expect(compiled.textContent).toContain('Username');
    expect(compiled.textContent).toContain('Register');
  });
});

function fillValidForm(component: RegisterComponent): void {
  component.fullname = 'Test User';
  component.username = 'testuser';
  component.email = 'test@email.com';
  component.phone = '123456789';
  component.password = 'password';
  component.confirmPassword = 'password';
}
