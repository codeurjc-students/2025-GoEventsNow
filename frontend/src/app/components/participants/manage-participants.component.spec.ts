import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ManageParticipantsComponent } from './manage-participants.component';
import { ParticipantService } from '../../service/participant.service';

const mockParticipants = [
  { id: 1, name: 'Bad Bunny', type: 'Music', biography: 'Great Artist' },
  { id: 2, name: 'Pablo Picasso', type: 'Painter', biography: 'Famous Painter' }
];

describe('ManageParticipantsComponent', () => {
  let component: ManageParticipantsComponent;
  let fixture: ComponentFixture<ManageParticipantsComponent>;
  let participantServiceMock: any;

  beforeEach(async () => {
    participantServiceMock = {
      findAll: vi.fn().mockReturnValue(of(mockParticipants)),
      deleteById: vi.fn().mockReturnValue(of({})),
      findById: vi.fn().mockReturnValue(of(mockParticipants[0]))
    };

    await TestBed.configureTestingModule({
      imports: [ManageParticipantsComponent],
      providers: [
        provideRouter([]),
        { provide: ParticipantService, useValue: participantServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ManageParticipantsComponent);
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

  it('should keep hasMore true when returned participants length equals size', () => {
    const tenParticipants = Array.from({ length: 10 }, (_, i) => ({
      id: i + 1,
      name: `Participant ${i + 1}`,
      type: 'Music',
      biography: 'Biography'
    }));

    participantServiceMock.findAll = vi.fn().mockReturnValue(of(tenParticipants));

    fixture = TestBed.createComponent(ManageParticipantsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.participants.length).toBe(10);
    expect(component.page).toBe(1);
    expect(component.hasMore).toBe(true);
  });

  it('should delete participant and remove it from list', () => {
    component.participants = [...mockParticipants];

    component.deleteParticipant(1);

    expect(participantServiceMock.deleteById).toHaveBeenCalledWith(1);
    expect(component.participants.length).toBe(1);
    expect(component.participants[0].id).toBe(2);
  });

  it('should show participant name when delete fails', () => {
    participantServiceMock.deleteById = vi.fn().mockReturnValue(throwError(() => new Error('Delete failed')));
    participantServiceMock.findById = vi.fn().mockReturnValue(of(mockParticipants[0]));

    component.deleteParticipant(1);

    expect(component.alertMessage).toBe(true);
    expect(component.alertMessageText).toBe(
      'Failed to delete participant Bad Bunny, as it is currently in an event. Please try again later.'
    );
  });

  it('should render participant names in DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Bad Bunny');
    expect(compiled.textContent).toContain('Pablo Picasso');
  });
});
