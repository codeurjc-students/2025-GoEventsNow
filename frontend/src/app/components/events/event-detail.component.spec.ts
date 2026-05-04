import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EventDetailComponent } from './event-detail.component';
import { EventService } from '../../service/event.service';
import { firstValueFrom, of } from 'rxjs';
import { ActivatedRoute, provideRouter } from '@angular/router';

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
    expect(event.title).toBe('Global Latin Music Festival');
    expect(event.location).toBe('Madrid, WiZink Center');
  });


  it('should render event details in the DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Global Latin Music Festival');
    expect(compiled.textContent).toContain('Madrid, WiZink Center');
    expect(compiled.textContent).toContain('Bad Bunny');
  });


});
