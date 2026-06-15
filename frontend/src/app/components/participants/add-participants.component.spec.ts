import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { AddParticipantsComponent } from './add-participants.component';
import { ParticipantService } from '../../service/participant.service';
import { Participant } from '../../model/participant';

const validParticipant: Participant = {
  id: 1,
  name: 'Bad Bunny',
  type: 'Music Artist',
  biography: 'Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.',
  participantImage: false
};

describe('AddParticipantsComponent', () => {
  let component: AddParticipantsComponent;
  let fixture: ComponentFixture<AddParticipantsComponent>;
  let participantServiceMock: any;
  let routerMock: any;

  beforeEach(async () => {
    participantServiceMock = {
      findById: vi.fn().mockReturnValue(of(validParticipant)),
      createOrReplaceParticipant: vi.fn().mockReturnValue(of(validParticipant)),
      createOrReplaceParticipantImage: vi.fn().mockReturnValue(of(validParticipant)),
      deleteParticipantImage: vi.fn().mockReturnValue(of({}))
    };

    routerMock = {
      navigate: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [AddParticipantsComponent],
      providers: [
        { provide: ParticipantService, useValue: participantServiceMock },
        { provide: Router, useValue: routerMock },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              params: {}
            }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AddParticipantsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should start in create mode when there is no id route param', () => {
    expect(component.newParticipant).toBe(true);
    expect(participantServiceMock.findById).not.toHaveBeenCalled();
  });

  it('should validate a correct participant', () => {
    component.participant = { ...validParticipant };

    expect(component.isFormParticipantValid()).toBe(true);
  });

  it('should invalidate participant without name', () => {
    component.participant = { ...validParticipant, name: '' };

    expect(component.isFormParticipantValid()).toBe(false);
  });

  it('should invalidate participant when uploading and removing image', () => {
    component.participant = { ...validParticipant };
    component.imageFile = new File(['fake-image'], 'noImage.png', { type: 'image/png' });
    component.removeImage = true;

    expect(component.isFormParticipantValid()).toBe(false);
  });

  it('should not send when form is invalid', () => {
    component.participant = { ...validParticipant, biography: '' };

    component.send();

    expect(component.errorMessage).toBe('Please fill out all fields correctly.');
    expect(participantServiceMock.createOrReplaceParticipant).not.toHaveBeenCalled();
  });

  it('should create participant and navigate home when form is valid', () => {
    component.participant = { ...validParticipant };

    component.send();

    expect(participantServiceMock.createOrReplaceParticipant).toHaveBeenCalledWith(validParticipant);
    expect(routerMock.navigate).toHaveBeenCalledWith(['']);
  });

  it('should upload image after creating participant if imageFile exists', () => {
    const file = new File(['fake-image'], 'noImage.png', { type: 'image/png' });
    component.participant = { ...validParticipant };
    component.imageFile = file;

    component.send();

    expect(participantServiceMock.createOrReplaceParticipantImage).toHaveBeenCalledWith(validParticipant, file);
    expect(routerMock.navigate).toHaveBeenCalledWith(['']);
  });

  it('should delete image after saving participant if removeImage is true', () => {
    component.participant = { ...validParticipant, participantImage: true };
    component.removeImage = true;

    component.send();

    expect(participantServiceMock.deleteParticipantImage).toHaveBeenCalledWith(validParticipant);
    expect(routerMock.navigate).toHaveBeenCalledWith(['']);
  });

  it('should set imageFile on file selected', () => {
    const file = new File(['fake-image'], 'noImage.png', { type: 'image/png' });

    component.onFileSelected({
      target: {
        files: [file]
      }
    });

    expect(component.imageFile).toBe(file);
  });

  it('should handle create or replace participant error', () => {
    const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    vi.spyOn(component, 'isFormParticipantValid').mockReturnValue(true);
    (participantServiceMock.createOrReplaceParticipant as any).mockImplementationOnce(() => throwError(() => new Error('error')));

    component.send();

    expect(consoleErrorSpy).toHaveBeenCalledWith('Failed to create participant:', expect.any(Error));
    consoleErrorSpy.mockRestore();
  });

  it('should handle create or replace participant image error', () => {
    const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    vi.spyOn(component, 'isFormParticipantValid').mockReturnValue(true);
    component.removeImage = true;

    const mockReturnedParticipant = { id: 1, name: 'Artist' } as any;
    (participantServiceMock.createOrReplaceParticipant as any).mockImplementationOnce(() => of(mockReturnedParticipant));
    (participantServiceMock.deleteParticipantImage as any).mockImplementationOnce(() => throwError(() => new Error('error')));

    component.send();

    expect(consoleErrorSpy).toHaveBeenCalledWith('Failed to delete image:', expect.any(Error));
    consoleErrorSpy.mockRestore();
  });

  it('should handle upload participant image error', () => {
    const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    vi.spyOn(component, 'isFormParticipantValid').mockReturnValue(true);
    component.imageFile = new File(['fake-image'], 'test.png', { type: 'image/png' });

    const mockReturnedParticipant = { id: 1, name: 'Artist' } as any;
    (participantServiceMock.createOrReplaceParticipant as any).mockImplementationOnce(() => of(mockReturnedParticipant));
    (participantServiceMock.createOrReplaceParticipantImage as any).mockImplementationOnce(() => throwError(() => new Error('error')));

    component.send();

    expect(consoleErrorSpy).toHaveBeenCalledWith('Failed to upload image:', expect.any(Error));
    consoleErrorSpy.mockRestore();
  });

  it('should load participant data and set newParticipant to false when participantId is present', () => {
    const mockActivatedRoute = {
      snapshot: { params: { id: '42' } }
    } as any;

    const mockReturnedParticipant = {
      id: 42,
      name: 'Bad Bunny',
      type: 'Music Artist',
      biography: 'Bio text',
      numFollowers: 120
    } as any;

    (participantServiceMock.findById as any).mockImplementationOnce(() => of(mockReturnedParticipant));

    const manualComponent = new AddParticipantsComponent(
      mockActivatedRoute,
      routerMock,
      participantServiceMock,
      { detectChanges: vi.fn() } as any
    );

    expect(manualComponent.newParticipant).toBe(false);
    expect(participantServiceMock.findById).toHaveBeenCalledWith('42');
    expect(manualComponent.participant.name).toBe('Bad Bunny');
    expect(manualComponent.participant.numFollowers).toBe(120);
  });

  it('should render participant form correctly', () => {
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const formElement = compiled.querySelector('form');

    expect(formElement).toBeTruthy();
  });

});
