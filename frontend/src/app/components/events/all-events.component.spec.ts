import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { AllEventsComponent } from './all-events.component';
import { EventService } from '../../service/event.service';
import { provideRouter } from '@angular/router';

const mockEvents10 = [
    { id: 1, title: "Spring Boot 4.0 Workshop" },
    { id: 2, title: "Art Exhibition" },
    { id: 3, title: "Basketball Tournament" },
    { id: 4, title: "Keynote: Future of AI" },
    { id: 5, title: "Motomami World Tour" },
    { id: 6, title: "Avant-Garde Cooking Masterclass" },
    { id: 7, title: "World Cup Final" },
    { id: 8, title: "International Science Congress" },
    { id: 9, title: "Movie Premiere" },
    { id: 10, title: "Roland Garros Final" }
];

const mockEvents9 = mockEvents10.slice(0, 9);


describe('AllEventsComponent', () => {

    let component: AllEventsComponent;
    let fixture: ComponentFixture<AllEventsComponent>;
    let eventServiceMock: Partial<EventService>;

    beforeEach(async () => {

        eventServiceMock = {
            findAll: vi.fn().mockReturnValue(of(mockEvents10))
        };

        await TestBed.configureTestingModule({
            imports: [AllEventsComponent],
            providers: [
                provideRouter([]),
                { provide: EventService, useValue: eventServiceMock }
            ]
        }).compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(AllEventsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create the component', () => {
        expect(component).toBeTruthy();
    });

    it('should initialize events$ in ngOnInit', () => {
        expect(eventServiceMock.findAll).toHaveBeenCalledWith(0, 10);
        expect(component.page).toBe(1);
        expect(eventServiceMock.findAll).toHaveBeenCalledTimes(1);
    });

    it('should set hasMore false when events are less than size', () => {
        eventServiceMock.findAll = vi.fn().mockReturnValue(of(mockEvents9));

        fixture = TestBed.createComponent(AllEventsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();

        expect(component.events.length).toBe(9);
        expect(component.hasMore).toBe(false);
    });

    it('should keep hasMore true when events length equals size', () => {
        eventServiceMock.findAll = vi.fn().mockReturnValue(of(mockEvents10));

        fixture = TestBed.createComponent(AllEventsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();

        expect(component.events.length).toBe(10);
        expect(component.hasMore).toBe(true);
    });

    it('should render event titles in the DOM', () => {
        const compiled = fixture.nativeElement as HTMLElement;

        expect(compiled.textContent).toContain('Spring Boot 4.0 Workshop');
        expect(compiled.textContent).toContain('Art Exhibition');
        expect(compiled.textContent).toContain('Basketball Tournament');
        expect(compiled.textContent).toContain('Keynote: Future of AI');
        expect(compiled.textContent).toContain('Motomami World Tour');
        expect(compiled.textContent).toContain('Avant-Garde Cooking Masterclass');
        expect(compiled.textContent).toContain('World Cup Final');
        expect(compiled.textContent).toContain('International Science Congress');
        expect(compiled.textContent).toContain('Movie Premiere');
        expect(compiled.textContent).toContain('Roland Garros Final');
    });


});
