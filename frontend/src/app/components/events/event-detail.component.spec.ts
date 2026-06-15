import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EventDetailComponent } from './event-detail.component';
import { EventService } from '../../service/event.service';
import { firstValueFrom, of, throwError } from 'rxjs';
import { ActivatedRoute, provideRouter, Router } from '@angular/router';
import { Review } from '../../model/review';
import { ReviewService } from '../../service/review.service';
import { UserService } from '../../service/user.service';
import { User } from '../../model/user';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

const mockEvent = {
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
  availableVipTickets: 1000,
  image: true,
  participants: [
    {
      id: 1,
      name: 'Bad Bunny',
      type: 'Music Artist',
      biography: 'Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.',
      participantImage: true
    }
  ],
  tickets: [],
  reviews: []
};

const mockReview: Review = {
  id: 1,
  description: 'Great event',
  rating: 4.5,
  eventAssociatedId: 1,
  userOwnerId: 1
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
  ],
  favoriteEvents: []
} as User;

describe('EventDetailComponent', () => {
  let component: EventDetailComponent;
  let fixture: ComponentFixture<EventDetailComponent>;
  let eventServiceMock: any;
  let reviewServiceMock: any;
  let userServiceMock: any;

  beforeEach(async () => {
    eventServiceMock = {
      findById: vi.fn().mockImplementation(() => of(mockEvent))
    };

    reviewServiceMock = {
      createOrReplaceReview: vi.fn().mockImplementation(() => of(mockReview)),
      getAllReviewsForEvent: vi.fn().mockImplementation(() => of([mockReview]))
    };

    userServiceMock = {
      getCurrentUser: vi.fn().mockImplementation(() => of(mockUser)),
      findById: vi.fn().mockImplementation(() => of(mockUser)),
      addFavoriteEvent: vi.fn().mockImplementation(() => of({})),
      removeFavoriteEvent: vi.fn().mockImplementation(() => of({}))
    };

    await TestBed.configureTestingModule({
      imports: [EventDetailComponent],
      providers: [
        provideRouter([]),
        { provide: EventService, useValue: eventServiceMock },
        { provide: ReviewService, useValue: reviewServiceMock },
        { provide: UserService, useValue: userServiceMock },
        { provide: ActivatedRoute, useValue: { snapshot: { params: { id: 1 } } } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(EventDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize events$ in ngOnInit', () => {
    expect(eventServiceMock.findById).toHaveBeenCalledWith(1);
    expect(eventServiceMock.findById).toHaveBeenCalledTimes(1);
  });

  it('should have event$', async () => {
    const event = await firstValueFrom(component.event$);

    expect(event.title).toBe('Global Latin Music Festival');
    expect(event.location).toBe('Madrid, WiZink Center');
  });

  it('should render event details in the DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Global Latin Music Festival');
    expect(compiled.textContent).toContain('Madrid, WiZink Center');
    expect(compiled.textContent).toContain('Bad Bunny');
  });

  it('should save review', () => {
    component.reviews = [mockReview];
    component.activeReview = { ...mockReview };

    component.saveReviewUpdate();

    expect(reviewServiceMock.createOrReplaceReview).toHaveBeenCalledWith(component.activeReview);
    expect(component.reviews[0].description).toBe('Great event');
  });

  it('should add new review when active review has no id', () => {
    const newReview = {
      ...mockReview,
      id: 2,
      description: 'New review'
    };

    reviewServiceMock.createOrReplaceReview.mockImplementationOnce(() => of(newReview));

    component.reviews = [mockReview];
    component.currentUser = mockUser;
    component.activeReview = {
      description: 'New review',
      rating: 5,
      eventAssociatedId: 1,
      userOwnerId: 1
    } as Review;

    component.saveReviewUpdate();

    expect(component.reviews[0].description).toBe('New review');
    expect(component.reviews.length).toBe(2);
  });

  it('should clear current user and favorite state when current user loading fails', () => {
    userServiceMock.getCurrentUser.mockImplementationOnce(() => throwError(() => new Error('error')));

    const errorFixture = TestBed.createComponent(EventDetailComponent);
    const errorComponent = errorFixture.componentInstance;
    errorFixture.detectChanges();

    expect(errorComponent.currentUser).toBeNull();
    expect(errorComponent.isFavorite).toBe(false);
  });

  it('should return when user is null while toggling favorite', () => {
    component.currentUser = null;
    component.currentEventId = 1;

    component.toggleFavorite();

    expect(userServiceMock.addFavoriteEvent).not.toHaveBeenCalled();
    expect(userServiceMock.removeFavoriteEvent).not.toHaveBeenCalled();
  });

  it('should add favorite event', () => {
    component.currentUser = mockUser;
    component.currentEventId = 1;
    component.isFavorite = false;

    component.toggleFavorite();

    expect(userServiceMock.addFavoriteEvent).toHaveBeenCalledWith(1, 1);
    expect(component.isFavorite).toBe(true);
  });

  it('should remove favorite event', () => {
    component.currentUser = mockUser;
    component.currentEventId = 1;
    component.isFavorite = true;

    component.toggleFavorite();

    expect(userServiceMock.removeFavoriteEvent).toHaveBeenCalledWith(1, 1);
    expect(component.isFavorite).toBe(false);
  });

  it('should handle save review error', () => {
    const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    reviewServiceMock.createOrReplaceReview.mockImplementationOnce(() => throwError(() => new Error('error')));

    component.activeReview = {} as Review;

    component.saveReviewUpdate();

    expect(consoleErrorSpy).toHaveBeenCalled();

    consoleErrorSpy.mockRestore();
  });

  it('should handle add favorite error', () => {
    const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    userServiceMock.addFavoriteEvent.mockImplementationOnce(() => throwError(() => new Error('error')));

    component.currentUser = mockUser;
    component.currentEventId = 1;
    component.isFavorite = false;

    component.toggleFavorite();

    expect(consoleErrorSpy).toHaveBeenCalled();

    consoleErrorSpy.mockRestore();
  });

  it('should handle remove favorite error', () => {
    const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    userServiceMock.removeFavoriteEvent.mockImplementationOnce(() => throwError(() => new Error('error')));

    component.currentUser = mockUser;
    component.currentEventId = 1;
    component.isFavorite = true;

    component.toggleFavorite();

    expect(consoleErrorSpy).toHaveBeenCalled();

    consoleErrorSpy.mockRestore();
  });

  it('should open review modal for a new review', async () => {
    component.currentUser = mockUser;

    const saveSpy = vi.spyOn(component, 'saveReviewUpdate');
    const modalOpenSpy = vi.spyOn(component.modalService, 'open').mockReturnValue({
      result: Promise.resolve('Save click')
    } as any);

    component.openReviewModal({} as any, 1);

    expect(component.activeReview.description).toBe('');
    expect(component.activeReview.rating).toBe(0);
    expect(component.activeReview.eventAssociatedId).toBe(1);
    expect(component.activeReview.userOwnerId).toBe(1);

    await Promise.resolve();

    expect(modalOpenSpy).toHaveBeenCalled();
    expect(saveSpy).toHaveBeenCalled();
  });

  it('should open review modal for editing an existing review', () => {
    const modalOpenSpy = vi.spyOn(component.modalService, 'open').mockReturnValue({
      result: Promise.resolve('Cancel')
    } as any);

    component.openReviewModal({} as any, 1, mockReview);

    expect(component.activeReview).toEqual(mockReview);
    expect(component.activeReview).not.toBe(mockReview);
    expect(modalOpenSpy).toHaveBeenCalledWith(
      {},
      { ariaLabelledBy: 'modal-basic-title', centered: true }
    );
  });
});