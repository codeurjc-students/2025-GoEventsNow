import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { firstValueFrom, of, throwError } from 'rxjs';
import { ParticipantDetailComponent } from './participant-detail.component';
import { ParticipantService } from '../../service/participant.service';
import { EventService } from '../../service/event.service';
import { UserService } from '../../service/user.service';

const mockParticipant = {
  id: 1,
  name: 'Bad Bunny',
  type: 'Music Artist',
  biography: 'Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.',
  participantImage: true
};

const mockEvents = [
  {
    id: 1,
    title: 'Global Latin Music Festival',
    description: 'A large-scale live music festival bringing together leading Latin and international artists.',
    category: 'Music',
    location: 'Madrid, WiZink Center',
    date: '2026-05-10',
    time: '21:00',
    basicPrice: 80,
    vipPrice: 250,
    availableBasicTickets: 15000,
    availableVipTickets: 1000
  }
];

const mockUser = {
  id: 1,
  fullname: 'Test User',
  username: 'testuser',
  phone: 123456789,
  email: 'test@email.com',
  password: '',
  numTicketsBought: 0,
  favoriteGenre: 'None',
  followedParticipants: []
};

describe('ParticipantDetailComponent', () => {
  let component: ParticipantDetailComponent;
  let fixture: ComponentFixture<ParticipantDetailComponent>;
  let participantServiceMock: any;
  let eventServiceMock: any;
  let userServiceMock: any;

  beforeEach(async () => {
    participantServiceMock = {
      findById: vi.fn().mockReturnValue(of(mockParticipant))
    };

    eventServiceMock = {
      getEventsByParticipantId: vi.fn().mockReturnValue(of(mockEvents))
    };

    userServiceMock = {
      getCurrentUser: vi.fn().mockReturnValue(of(mockUser)),
      followParticipant: vi.fn().mockReturnValue(of({})),
      unfollowParticipant: vi.fn().mockReturnValue(of({}))
    };

    await TestBed.configureTestingModule({
      imports: [ParticipantDetailComponent],
      providers: [
        provideRouter([]),
        { provide: ParticipantService, useValue: participantServiceMock },
        { provide: EventService, useValue: eventServiceMock },
        { provide: UserService, useValue: userServiceMock },
        { provide: ActivatedRoute, useValue: { snapshot: { params: { id: 1 } } } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ParticipantDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize participant$ in ngOnInit', () => {
    expect(participantServiceMock.findById).toHaveBeenCalledWith(1);
    expect(participantServiceMock.findById).toHaveBeenCalledTimes(1);
  });

  it('should load events for participant on init', () => {
    expect(eventServiceMock.getEventsByParticipantId).toHaveBeenCalledWith(1);
    expect(component.events).toEqual(mockEvents);
  });

  it('should have participant$', async () => {
    const participant = await firstValueFrom(component.participant$);

    expect(participant.name).toBe('Bad Bunny');
    expect(participant.type).toBe('Music Artist');
  });

  it('should render participant and event details in the DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Bad Bunny');
    expect(compiled.textContent).toContain('Puerto Rican global superstar known for redefining reggaeton and Latin trap');
    expect(compiled.textContent).toContain('Global Latin Music Festival');
  });


  it('should follow participant', () => {
    component.currentUser = mockUser;
    component.currentParticipantId = 1;
    component.isFollowing = false;
    component.displayFollowerCount = 10;

    component.toggleFollow();

    expect(userServiceMock.followParticipant).toHaveBeenCalledWith(1, 1);
    expect(component.isFollowing).toBe(true);
    expect(component.displayFollowerCount).toBe(11);
  });

  it('should unfollow participant', () => {
    component.currentUser = mockUser;
    component.currentParticipantId = 1;
    component.isFollowing = true;
    component.displayFollowerCount = 10;

    component.toggleFollow();

    expect(userServiceMock.unfollowParticipant).toHaveBeenCalledWith(1, 1);
    expect(component.isFollowing).toBe(false);
    expect(component.displayFollowerCount).toBe(9);
  });

  it('should not follow or unfollow if user is not logged in', () => {
    component.currentUser = null;
    component.currentParticipantId = 1;

    component.toggleFollow();

    expect(userServiceMock.followParticipant).not.toHaveBeenCalled();
    expect(userServiceMock.unfollowParticipant).not.toHaveBeenCalled();
  });

  it('should handle current user is null load error', () => {
    userServiceMock.getCurrentUser.mockImplementationOnce(() => throwError(() => new Error('error')));

    const errorFixture = TestBed.createComponent(ParticipantDetailComponent);
    const errorComponent = errorFixture.componentInstance;
    errorFixture.detectChanges();

    expect(errorComponent.currentUser).toBeNull();
    expect(errorComponent.isFollowing).toBe(false);
  });

  it('should handle participant loading error', () => {
    const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    participantServiceMock.findById.mockImplementationOnce(() => of(mockParticipant));
    eventServiceMock.getEventsByParticipantId.mockImplementationOnce(() => throwError(() => new Error('error')));

    component.ngOnInit();

    expect(consoleErrorSpy).toHaveBeenCalled();
    consoleErrorSpy.mockRestore();
  });

  it('should handle follow participant error', () => {
    const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    component.currentUser = mockUser;
    component.currentParticipantId = 1;
    component.isFollowing = false;
    userServiceMock.followParticipant.mockImplementationOnce(() => throwError(() => new Error('error')));

    component.toggleFollow();

    expect(consoleErrorSpy).toHaveBeenCalledWith('Failed to follow participant', expect.any(Error));
    consoleErrorSpy.mockRestore();
  });

  it('should handle unfollow participant error', () => {
    const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    component.currentUser = mockUser;
    component.currentParticipantId = 1;
    component.isFollowing = true;
    userServiceMock.unfollowParticipant.mockImplementationOnce(() => throwError(() => new Error('error')));

    component.toggleFollow();

    expect(consoleErrorSpy).toHaveBeenCalledWith('Failed to unfollow participant', expect.any(Error));
    consoleErrorSpy.mockRestore();
  });

});
