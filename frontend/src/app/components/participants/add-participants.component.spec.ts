import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
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
});
