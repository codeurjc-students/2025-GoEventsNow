import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EventListComponent } from './components/events/event-list.component';
import { EventService } from './service/event.service';
import { of } from 'rxjs';
import { Event } from './model/event';
import { ActivatedRoute } from '@angular/router';


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
