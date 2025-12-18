import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { EventService } from './service/event.service';
import { of } from 'rxjs';
import { Event } from './model/event';


const mockEvents: Event[] = [
                {
                    "id": 1,
                    "title": "Taller de Spring Boot 4.0",
                    "category": "Tecnología",
                    "location": "Fuenlabrada, Madrid",
                    "date": "15-03-2026"
                },
                {
                    "id": 2,
                    "title": "Exposición Arte",
                    "category": "Cultura",
                    "location": "Barcelona",
                    "date": "12-02-2026"
                },
                {
                    "id": 3,
                    "title": "Torneo de Baloncesto",
                    "category": "Deportes",
                    "location": "Getafe, Madrid",
                    "date": "02-08-2026"
                }
        ];

describe('AppComponent', () => {

  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let eventServiceMock: Partial<EventService>;

  beforeEach(async () => {

    eventServiceMock = {
      findAll: vi.fn().mockReturnValue(of(mockEvents))
    };
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        { provide: EventService, useValue: eventServiceMock }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
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
    component.events$.subscribe( (events) => {
      expect(events.length).toBe(3);
      expect(events).toEqual(mockEvents);
    })
  });
  

});
