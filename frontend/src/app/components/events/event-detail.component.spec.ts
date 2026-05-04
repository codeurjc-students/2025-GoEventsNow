import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EventDetailComponent } from './event-detail.component';
import { EventService } from '../../service/event.service';
import { firstValueFrom, of } from 'rxjs';
import { ActivatedRoute, provideRouter } from '@angular/router';

const mockEvent = {
  id: 1,
  title: 'Spring Boot 4.0 Workshop',
  description: "Intensive workshop on the framework's new features.",
  category: 'Technology',
  location: 'Fuenlabrada, Madrid',
  date: '2026-03-15',
  time: '10:00',
  basicPrice: 50,
  vipPrice: 120,
  availableBasicTickets: 100,
  availableVipTickets: 20,
  image: true,
  participants: [
    {
      id: 1,
      name: 'Bad Bunny',
      type: 'Music',
      biography: 'Great Artist',
      participantImage: true
    }
  ],
  tickets: []
};


describe('EventDetailComponent', () => {

  let component: EventDetailComponent;
  let fixture: ComponentFixture<EventDetailComponent>;
  let eventServiceMock: Partial<EventService>;

  beforeEach(async () => {

    eventServiceMock = {
      findById: vi.fn().mockReturnValue(of(mockEvent))
    };

    await TestBed.configureTestingModule({
      imports: [EventDetailComponent],
      providers: [
        provideRouter([]),
        { provide: EventService, useValue: eventServiceMock },
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
    expect(event.title).toBe('Spring Boot 4.0 Workshop');
    expect(event.location).toBe('Fuenlabrada, Madrid');
  });


  it('should render event details in the DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Spring Boot 4.0 Workshop');
    expect(compiled.textContent).toContain('Fuenlabrada, Madrid');
    expect(compiled.textContent).toContain('Bad Bunny');
  });


});
