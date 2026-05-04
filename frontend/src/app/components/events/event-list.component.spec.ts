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
    title: "Global Latin Music Festival",
    description: "A large-scale live music festival bringing together leading Latin and international artists.",
    category: "Music",
    location: "Madrid, WiZink Center",
    date: "2026-05-10",
    time: "21:00",
    basicPrice: 80.0,
    vipPrice: 250.0,
    availableBasicTickets: 15000,
    availableVipTickets: 1000
  },
  {
    id: 2,
    title: "Olivia Rodrigo Concert Experience",
    description: "A concert experience focused on emotional songwriting, acoustic arrangements and powerful live vocals.",
    category: "Music",
    location: "Barcelona, Palau Sant Jordi",
    date: "2026-06-14",
    time: "20:30",
    basicPrice: 55.0,
    vipPrice: 160.0,
    availableBasicTickets: 9000,
    availableVipTickets: 600
  },
  {
    id: 3,
    title: "Stand-Up Comedy Night: Juan Dávila Live",
    description: "A live comedy show full of improvisation, audience interaction and unpredictable moments.",
    category: "Comedy",
    location: "Fuenlabrada, Madrid",
    date: "2026-03-15",
    time: "21:00",
    basicPrice: 25.0,
    vipPrice: 60.0,
    availableBasicTickets: 500,
    availableVipTickets: 80
  }
];

const mockParticipants = [
  {
    id: 1,
    name: "Bad Bunny",
    type: "Music Artist",
    biography: "Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.",
  },
  {
    id: 2,
    name: "Olivia Rodrigo",
    type: "Music Artist",
    biography: "Grammy-winning artist recognized for emotional songwriting, powerful vocals and chart-topping pop-rock performances.",
  },
  {
    id: 3,
    name: "Juan Dávila",
    type: "Comedian",
    biography: "Spanish stand-up comedian known for his interactive, provocative and improvisational comedy shows.",
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
    expect(events[0].title).toBe('Global Latin Music Festival');
    expect(events[0].location).toBe('Madrid, WiZink Center');
  });

  it('should have participants$', async () => {
    const participants = await firstValueFrom(component.participants$);
    expect(participants[0].name).toBe('Bad Bunny');
    expect(participants.length).toBe(3);
  });

  it('should render event titles in the DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Global Latin Music Festival');
    expect(compiled.textContent).toContain('Olivia Rodrigo Concert Experience');
    expect(compiled.textContent).toContain('Stand-Up Comedy Night: Juan Dávila Live');
  });


});
