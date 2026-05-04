import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { ParticipantsListComponent } from './participants-list.component';
import { ParticipantService } from '../../service/participant.service';

const mockParticipants = [
  { id: 1, name: 'Bad Bunny', type: 'Music Artist', biography: 'Puerto Rican global superstar known for redefining reggaeton and Latin trap.' },
  { id: 2, name: 'Olivia Rodrigo', type: 'Music Artist', biography: 'Grammy-winning artist recognized for emotional songwriting and chart-topping pop-rock performances.' },
  { id: 3, name: 'Juan Dávila', type: 'Comedian', biography: 'Spanish stand-up comedian known for his interactive and improvisational comedy shows.' }
];

describe('ParticipantsListComponent', () => {
  let component: ParticipantsListComponent;
  let fixture: ComponentFixture<ParticipantsListComponent>;
  let participantServiceMock: any;

  beforeEach(async () => {
    participantServiceMock = {
      findAll: vi.fn().mockReturnValue(of(mockParticipants))
    };

    await TestBed.configureTestingModule({
      imports: [ParticipantsListComponent],
      providers: [
        provideRouter([]),
        { provide: ParticipantService, useValue: participantServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ParticipantsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should load participants on init', () => {
    expect(participantServiceMock.findAll).toHaveBeenCalledWith(0, 10);
    expect(component.participants).toEqual(mockParticipants);
    expect(component.page).toBe(1);
  });

  it('should set hasMore false when returned participants are less than size', () => {
    expect(component.hasMore).toBe(false);
  });

  it('should not load participants if hasMore is false', () => {
    vi.clearAllMocks();

    component.hasMore = false;
    component.loadParticipants();

    expect(participantServiceMock.findAll).not.toHaveBeenCalled();
  });

  it('should render participant names in DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Bad Bunny');
    expect(compiled.textContent).toContain('Olivia Rodrigo');
    expect(compiled.textContent).toContain('Juan Dávila');
  });
});
