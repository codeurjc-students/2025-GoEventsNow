import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EventListComponent } from './components/events/event-list.component';
import { EventService } from './service/event.service';
import { of } from 'rxjs';
import { Event } from './model/event';
import { ActivatedRoute } from '@angular/router';


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

describe('EventListComponent', () => {

  let component: EventListComponent;
  let fixture: ComponentFixture<EventListComponent>;
  let eventServiceMock: Partial<EventService>;

  beforeEach(async () => {

    eventServiceMock = {
      findAll: vi.fn().mockReturnValue(of(mockEvents))
    };
    await TestBed.configureTestingModule({
      imports: [EventListComponent],
      providers: [
        { provide: EventService, useValue: eventServiceMock },
        { provide: ActivatedRoute, useValue: {} }
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

  it('should initialize events$ in ngOnInit ', () => {
    expect(eventServiceMock.findAll).toHaveBeenCalledTimes(1);
  });

  it('should have events$ ', () => {
    component.events$.subscribe((events: Event[]) => {
      expect(events.length).toBe(3);
      expect(events).toEqual(mockEvents);
    })
  });


});
