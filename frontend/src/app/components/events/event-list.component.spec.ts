import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EventListComponent } from './event-list.component';
import { EventService } from '../../service/event.service';
import { firstValueFrom, of } from 'rxjs';
import { Event } from '../../model/event';
import { ParticipantService } from '../../service/participant.service';
import { provideRouter } from '@angular/router';


const mockEvents: Event[] = [
  {
    id: 1,
    title: "Spring Boot 4.0 Workshop",
    description: "Intensive workshop on the framework's new features.",
    category: "Technology",
    location: "Fuenlabrada, Madrid",
    date: "2026-03-15",
    time: "10:00",
    basicPrice: 50.0,
    vipPrice: 120.0,
    availableBasicTickets: 100,
    availableVipTickets: 20
  },
  {
    id: 2,
    title: "Art Exhibition",
    description: "International contemporary art exhibition.",
    category: "Culture",
    location: "Barcelona",
    date: "2026-02-12",
    time: "17:00",
    basicPrice: 15.0,
    vipPrice: 40.0,
    availableBasicTickets: 200,
    availableVipTickets: 50
  },
  {
    id: 3,
    title: "Basketball Tournament",
    description: "Regional amateur team competition.",
    category: "Sports",
    location: "Getafe, Madrid",
    date: "2026-08-02",
    time: "09:30",
    basicPrice: 10.0,
    vipPrice: 25.0,
    availableBasicTickets: 500,
    availableVipTickets: 50
  }
];

const mockParticipants = [
  {
    id: 1,
    name: "Bad Bunny",
    type: "Music",
    biography: "Great Artist",
  },
  {
    id: 2,
    name: "Pablo Picasso",
    type: "Painter",
    biography: "Famous Painter",
  },
  {
    id: 3,
    name: "Michael Jordan",
    type: "Basketball Player",
    biography: "Legendary Basketball Player",
  }
]

describe('EventListComponent', () => {

  let component: EventListComponent;
  let fixture: ComponentFixture<EventListComponent>;
  let eventServiceMock: Partial<EventService>;
  let participantServiceMock: Partial<ParticipantService>;

  beforeEach(async () => {

    eventServiceMock = {
      findAll: vi.fn().mockReturnValue(of(mockEvents))
    };
    participantServiceMock = {
      findAll: vi.fn().mockReturnValue(of(mockParticipants))
    };
    await TestBed.configureTestingModule({
      imports: [EventListComponent],
      providers: [
        provideRouter([]),
        { provide: EventService, useValue: eventServiceMock },
        { provide: ParticipantService, useValue: participantServiceMock }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EventListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize events$ in ngOnInit', () => {
    expect(eventServiceMock.findAll).toHaveBeenCalledWith(0, 3);
    expect(eventServiceMock.findAll).toHaveBeenCalledTimes(1);
  });

  it('should initialize participants$ in ngOnInit', () => {
    expect(participantServiceMock.findAll).toHaveBeenCalledWith(0, 3);
    expect(participantServiceMock.findAll).toHaveBeenCalledTimes(1);
  });

  it('should have event$', async () => {
    const events = await firstValueFrom(component.events$);

    expect(events.length).toBe(3);
    expect(events[0].title).toBe('Spring Boot 4.0 Workshop');
    expect(events[0].location).toBe('Fuenlabrada, Madrid');
  });

  it('should have participants$', async () => {
    const participants = await firstValueFrom(component.participants$);
    expect(participants[0].name).toBe('Bad Bunny');
    expect(participants.length).toBe(3);
  });

  it('should render event titles in the DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Spring Boot 4.0 Workshop');
    expect(compiled.textContent).toContain('Art Exhibition');
    expect(compiled.textContent).toContain('Basketball Tournament');
  });


});
