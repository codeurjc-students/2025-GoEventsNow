import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { firstValueFrom, of } from 'rxjs';
import { ParticipantDetailComponent } from './participant-detail.component';
import { ParticipantService } from '../../service/participant.service';
import { EventService } from '../../service/event.service';

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

describe('ParticipantDetailComponent', () => {
  let component: ParticipantDetailComponent;
  let fixture: ComponentFixture<ParticipantDetailComponent>;
  let participantServiceMock: any;
  let eventServiceMock: any;

  beforeEach(async () => {
    participantServiceMock = {
      findById: vi.fn().mockReturnValue(of(mockParticipant))
    };

    eventServiceMock = {
      getEventsByParticipantId: vi.fn().mockReturnValue(of(mockEvents))
    };

    await TestBed.configureTestingModule({
      imports: [ParticipantDetailComponent],
      providers: [
        provideRouter([]),
        { provide: ParticipantService, useValue: participantServiceMock },
        { provide: EventService, useValue: eventServiceMock },
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
});
