import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap, Router } from '@angular/router';
import { of, Subject } from 'rxjs';
import { UserPageComponent } from './user-page.component';
import { UserService } from '../../service/user.service';
import { EventService } from '../../service/event.service';
import { User } from '../../model/user';
import { Event } from '../../model/event';

const mockEvent: Event = {
  id: 1,
  title: 'Global Latin Music Festival',
  description: 'A large-scale live music festival bringing together leading Latin and international artists.',
  category: 'Music',
  location: 'Madrid, WiZink Center',
  date: '2026-05-10',
  time: '21:00',
  basicPrice: 50,
  vipPrice: 120,
  availableBasicTickets: 100,
  availableVipTickets: 20
};

const mockUser: User = {
  id: 1,
  fullname: 'Test User',
  username: 'testuser',
  email: 'test@email.com',
  phone: 123456789,
  password: '',
  numTicketsBought: 1,
  favoriteGenre: 'Technology',
  profileImage: false,
  roles: ['USER'],
  tickets: [
    {
      id: 1,
      ticketType: 'BASIC',
      price: 50,
      numTickets: 1,
      eventId: 1,
      userOwnerId: 1
    }
  ]
};

describe('UserPageComponent', () => {
  let component: UserPageComponent;
  let fixture: ComponentFixture<UserPageComponent>;
  let userServiceMock: any;
  let eventServiceMock: any;
  let routerMock: any;
  let currentUser$: Subject<User>;

  beforeEach(async () => {
    currentUser$ = new Subject<User>();

    userServiceMock = {
      getCurrentUser: vi.fn().mockReturnValue(currentUser$.asObservable()),
      replaceUser: vi.fn().mockReturnValue(of(mockUser)),
      createOrReplaceUserImage: vi.fn().mockReturnValue(of(mockUser)),
      deleteUserImage: vi.fn().mockReturnValue(of({}))
    };

    eventServiceMock = {
      findById: vi.fn().mockReturnValue(of(mockEvent))
    };

    routerMock = {
      navigate: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [UserPageComponent],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: EventService, useValue: eventServiceMock },
        { provide: Router, useValue: routerMock },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({ id: '1' })
            }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(UserPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    currentUser$.next(mockUser);
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should load current user from route id', () => {
    expect(userServiceMock.getCurrentUser).toHaveBeenCalled();
    expect(component.user.username).toBe('testuser');
    expect(component.tickets.length).toBe(1);
  });

  it('should load events for user tickets', () => {
    expect(eventServiceMock.findById).toHaveBeenCalledWith(1);
    expect(component.eventsMap.get(1)).toEqual(mockEvent);
  });

  it('should validate a correct user form', () => {
    component.user = { ...mockUser };

    expect(component.isFormUserValid()).toBe(true);
  });

  it('should invalidate user without fullname', () => {
    component.user = { ...mockUser, fullname: '' };

    expect(component.isFormUserValid()).toBe(false);
  });

  it('should not send when form is invalid', () => {
    component.user = { ...mockUser, email: 'bad-email' };

    component.send();

    expect(component.errorMessage).toBe('Please fill out all fields correctly.');
    expect(userServiceMock.replaceUser).not.toHaveBeenCalled();
  });

  it('should update user and navigate home when form is valid', () => {
    component.user = { ...mockUser };

    component.send();

    expect(userServiceMock.replaceUser).toHaveBeenCalledWith(mockUser);
    expect(routerMock.navigate).toHaveBeenCalledWith(['']);
  });

  it('should upload image after updating user if profileImage exists', () => {
    const file = new File(['fake-image'], 'profile.jpg', { type: 'image/jpeg' });
    component.user = { ...mockUser };
    component.profileImage = file;

    component.send();

    expect(userServiceMock.createOrReplaceUserImage).toHaveBeenCalledWith(mockUser, file);
    expect(routerMock.navigate).toHaveBeenCalledWith(['']);
  });

  it('should delete image after updating user if removeImage is true', () => {
    const userWithImage = { ...mockUser, profileImage: true };
    userServiceMock.replaceUser = vi.fn().mockReturnValue(of(userWithImage));
    component.user = userWithImage;
    component.removeImage = true;

    component.send();

    expect(userServiceMock.deleteUserImage).toHaveBeenCalledWith(userWithImage);
    expect(routerMock.navigate).toHaveBeenCalledWith(['']);
  });

  it('should set profileImage on file selected', () => {
    const file = new File(['fake-image'], 'profile.jpg', { type: 'image/jpeg' });

    component.onFileSelected({
      target: {
        files: [file]
      }
    });

    expect(component.profileImage).toBe(file);
  });

  it('should render user data in DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('testuser');
    expect(compiled.textContent).toContain('Test User');
    expect(compiled.textContent).toContain('test@email.com');
  });
});
