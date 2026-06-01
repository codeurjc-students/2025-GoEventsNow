import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EventDetailComponent } from './event-detail.component';
import { EventService } from '../../service/event.service';
import { firstValueFrom, of } from 'rxjs';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { Review } from '../../model/review';
import { ReviewService } from '../../service/review.service';
import { UserService } from '../../service/user.service';
import { User } from '../../model/user';

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
  tickets: []
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
  ]
};

describe('EventDetailComponent', () => {

  let component: EventDetailComponent;
  let fixture: ComponentFixture<EventDetailComponent>;
  let eventServiceMock: Partial<EventService>;
  let reviewServiceMock: any;
  let userServiceMock: Partial<UserService>;

  beforeEach(async () => {

    eventServiceMock = {
      findById: vi.fn().mockReturnValue(of(mockEvent))
    };

    reviewServiceMock = {
      createOrReplaceReview: vi.fn().mockReturnValue(of(mockReview)),
      getAllReviewsForEvent: vi.fn().mockReturnValue(of([mockReview])),
    };

    userServiceMock = {
      getCurrentUser: vi.fn().mockReturnValue(of(mockUser)),
      findById: vi.fn().mockReturnValue(of(mockUser))
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
  });

  beforeEach(() => {
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


});
