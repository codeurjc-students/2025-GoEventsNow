import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddEventComponent } from './add-event.component';
import { EventService } from '../../service/event.service';
import { ParticipantService } from '../../service/participant.service';
import { ActivatedRoute, Router } from '@angular/router';
import { defer, of, throwError } from 'rxjs';

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
} as any;

describe('AddEventComponent', () => {

    let component: AddEventComponent;
    let fixture: ComponentFixture<AddEventComponent>;
    let eventServiceMock: any;
    let participantServiceMock: any;
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

    it('should handle create or replace event error', () => {
        const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
        vi.spyOn(component, 'isFormEventValid').mockReturnValue(true);
        (eventServiceMock.createOrReplaceEvent as any).mockImplementationOnce(() => throwError(() => new Error('error')));

        component.send();

        expect(consoleErrorSpy).toHaveBeenCalled();
        consoleErrorSpy.mockRestore();
    });

    it('should handle create or replace event image error', () => {
        const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
        vi.spyOn(component, 'isFormEventValid').mockReturnValue(true);
        component.removeImage = true;

        (eventServiceMock.createOrReplaceEvent as any).mockImplementationOnce(() => of(validEvent));
        (eventServiceMock.deleteEventImage as any).mockImplementationOnce(() => throwError(() => new Error('error')));

        component.send();

        expect(consoleErrorSpy).toHaveBeenCalled();
        consoleErrorSpy.mockRestore();
    });

    it('should handle upload event image error', () => {
        const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
        vi.spyOn(component, 'isFormEventValid').mockReturnValue(true);
        component.imageFile = new File(['fake-image'], 'test.png', { type: 'image/png' });

        (eventServiceMock.createOrReplaceEvent as any).mockImplementationOnce(() => of(validEvent));
        (eventServiceMock.createOrReplaceEventImage as any).mockImplementationOnce(() => throwError(() => new Error('error')));

        component.send();

        expect(consoleErrorSpy).toHaveBeenCalled();
        consoleErrorSpy.mockRestore();
    });

    it('should load event data and set newEvent to false when eventId is present', () => {
        const mockActivatedRoute = {
            snapshot: { params: { id: '42' } }
        } as any;

        const mockReturnedEvent = {
            id: 42,
            title: 'Manual Edit Event',
            participants: [{ id: 10 }, { id: 20 }]
        } as any;

        (eventServiceMock.findById as any).mockImplementationOnce(() => of(mockReturnedEvent));

        const manualComponent = new AddEventComponent(
            mockActivatedRoute,
            routerMock,
            eventServiceMock,
            participantServiceMock,
            { detectChanges: vi.fn() } as any
        );

        expect(manualComponent.newEvent).toBe(false);
        expect(eventServiceMock.findById).toHaveBeenCalledWith('42');
        expect(manualComponent.event.title).toBe('Manual Edit Event');
        expect(manualComponent.selectedParticipants).toEqual([10, 20]);
    });


    it('should render event form correctly', () => {
        fixture.detectChanges();

        const compiled = fixture.nativeElement as HTMLElement;
        const formElement = compiled.querySelector('form');
        
        expect(formElement).toBeTruthy();
    });
});
