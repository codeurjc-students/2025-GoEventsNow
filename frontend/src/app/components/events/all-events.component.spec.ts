import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { AllEventsComponent } from './all-events.component';
import { EventService } from '../../service/event.service';
import { provideRouter } from '@angular/router';

const mockEvents10 = [
    { id: 1, title: "Global Latin Music Festival" },
    { id: 2, title: "Olivia Rodrigo Concert Experience" },
    { id: 3, title: "Stand-Up Comedy Night: Juan Dávila Live" },
    { id: 4, title: "Roland Garros Champions Exhibition" },
    { id: 5, title: "Future of Space and Artificial Intelligence" },
    { id: 6, title: "Cinema Masters: Storytelling and Performance" },
    { id: 7, title: "Elite Sports Legends Tournament" },
    { id: 8, title: "Top Dessert Masterclass" },
    { id: 9, title: "Basketball Leadership Camp" },
    { id: 10, title: "Young Hollywood Fan Convention" }
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

        expect(compiled.textContent).toContain('Global Latin Music Festival');
        expect(compiled.textContent).toContain('Olivia Rodrigo Concert Experience');
        expect(compiled.textContent).toContain('Stand-Up Comedy Night: Juan Dávila Live');
        expect(compiled.textContent).toContain('Roland Garros Champions Exhibition');
        expect(compiled.textContent).toContain('Future of Space and Artificial Intelligence');
        expect(compiled.textContent).toContain('Cinema Masters: Storytelling and Performance');
        expect(compiled.textContent).toContain('Elite Sports Legends Tournament');
        expect(compiled.textContent).toContain('Top Dessert Masterclass');
        expect(compiled.textContent).toContain('Basketball Leadership Camp');
        expect(compiled.textContent).toContain('Young Hollywood Fan Convention');
    });


});
