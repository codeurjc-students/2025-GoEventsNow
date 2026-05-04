import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddEventComponent } from './add-event.component';
import { EventService } from '../../service/event.service';
import { ParticipantService } from '../../service/participant.service';
import { ActivatedRoute, Router } from '@angular/router';
import { defer, of } from 'rxjs';

const mockParticipants = [
    { id: 1, name: 'Bad Bunny', type: 'Music Artist', biography: 'Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.' }
];

const validEvent = {
    id: 1,
    title: 'Test Event',
    description: 'Description',
    category: 'Music',
    location: 'Madrid',
    date: '2026-06-20',
    time: '18:00',
    basicPrice: 30,
    vipPrice: 80,
    availableBasicTickets: 150,
    availableVipTickets: 30,
    image: false,
    participants: [],
    tickets: []
};

describe('AddEventComponent', () => {

    let component: AddEventComponent;
    let fixture: ComponentFixture<AddEventComponent>;
    let eventServiceMock: Partial<EventService>;
    let participantServiceMock: Partial<ParticipantService>;
    let routerMock: any;

    beforeEach(async () => {
        eventServiceMock = {
            findById: vi.fn().mockReturnValue(of(validEvent)),
            createOrReplaceEvent: vi.fn().mockReturnValue(of(validEvent)),
            createOrReplaceEventImage: vi.fn().mockReturnValue(of(validEvent)),
            deleteEventImage: vi.fn().mockReturnValue(of({}))
        };

        participantServiceMock = {
            findAll: vi.fn().mockReturnValue(defer(() => Promise.resolve(mockParticipants)))
        };

        routerMock = {
            navigate: vi.fn()
        };

        await TestBed.configureTestingModule({
            imports: [AddEventComponent],
            providers: [
                { provide: EventService, useValue: eventServiceMock },
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

        fixture = TestBed.createComponent(AddEventComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
        await fixture.whenStable();
        fixture.detectChanges();
    });

    it('should create component', () => {
        expect(component).toBeTruthy();
    });

    it('should load participants', () => {
        expect(participantServiceMock.findAll).toHaveBeenCalledWith(0, 100);
        expect(component.allParticipants).toEqual(mockParticipants);
    });

    it('should start in create mode when there is no id route param', () => {
        expect(component.newEvent).toBe(true);
        expect(eventServiceMock.findById).not.toHaveBeenCalled();
    });

    it('should validate a correct event', () => {
        component.event = { ...validEvent };
        component.selectedParticipants = [1];

        expect(component.isFormEventValid()).toBe(true);
    });

    it('should invalidate event without title', () => {
        component.event = { ...validEvent, title: '' };
        component.selectedParticipants = [1];

        expect(component.isFormEventValid()).toBe(false);
    });

    it('should invalidate event without selected participants', () => {
        component.event = { ...validEvent };
        component.selectedParticipants = [];

        expect(component.isFormEventValid()).toBe(false);
    });

    it('should not send when form is invalid', () => {
        component.event = { ...validEvent, title: '' };
        component.selectedParticipants = [];

        component.send();

        expect(component.errorMessage).toBe('Please fill out all fields correctly.');
        expect(eventServiceMock.createOrReplaceEvent).not.toHaveBeenCalled();
    });

    it('should create event and navigate home when form is valid', () => {
        component.event = { ...validEvent };
        component.selectedParticipants = [1];
        component.allParticipants = mockParticipants;

        component.send();

        expect(eventServiceMock.createOrReplaceEvent).toHaveBeenCalled();
        expect(routerMock.navigate).toHaveBeenCalledWith(['']);
    });

    it('should upload image after creating event if imageFile exists', () => {
        const file = new File(['fake-image'], 'noImage.png', { type: 'image/png' });

        component.event = { ...validEvent };
        component.selectedParticipants = [1];
        component.allParticipants = mockParticipants;
        component.imageFile = file;

        component.send();

        expect(eventServiceMock.createOrReplaceEvent).toHaveBeenCalled();
        expect(eventServiceMock.createOrReplaceEventImage).toHaveBeenCalledWith(validEvent, file);
        expect(routerMock.navigate).toHaveBeenCalledWith(['']);
    });

    it('should delete image after saving event if removeImage is true', () => {
        component.event = { ...validEvent, image: true };
        component.selectedParticipants = [1];
        component.allParticipants = mockParticipants;
        component.removeImage = true;

        component.send();

        expect(eventServiceMock.createOrReplaceEvent).toHaveBeenCalled();
        expect(eventServiceMock.deleteEventImage).toHaveBeenCalledWith(validEvent);
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
