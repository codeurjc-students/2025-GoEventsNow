import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { AllEventsComponent } from './all-events.component';
import { EventService } from '../../service/event.service';
import { provideRouter } from '@angular/router';
import { UserService } from '../../service/user.service';

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

const mockUser = {
    id: 1,
    fullname: 'Test User',
    username: 'testuser',
    phone: 123456789,
    email: 'test@email.com',
    password: '',
    numTicketsBought: 0,
    favoriteGenre: 'None',
    favoriteEvents: [{ id: 1 }]
};


const mockEvents9 = mockEvents10.slice(0, 9);


describe('AllEventsComponent', () => {

    let component: AllEventsComponent;
    let fixture: ComponentFixture<AllEventsComponent>;
    let eventServiceMock: Partial<EventService>;
    let userServiceMock: any;

    beforeEach(async () => {

        eventServiceMock = {
            fetchEvents: vi.fn().mockReturnValue(of(mockEvents10))
        };

        userServiceMock = {
            getCurrentUser: vi.fn().mockReturnValue(of(mockUser)),
            addFavoriteEvent: vi.fn().mockReturnValue(of({})),
            removeFavoriteEvent: vi.fn().mockReturnValue(of({}))
        };

        await TestBed.configureTestingModule({
            imports: [AllEventsComponent],
            providers: [
                provideRouter([]),
                { provide: EventService, useValue: eventServiceMock },
                { provide: UserService, useValue: userServiceMock }
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

    it('should load events in ngOnInit', () => {
        expect(eventServiceMock.fetchEvents).toHaveBeenCalledWith({
            participantId: null,
            title: null,
            category: null,
            minPrice: null,
            maxPrice: null,
            sortBy: undefined,
            sortDir: 'desc',
            page: 0,
            size: 10
        });

        expect(component.page).toBe(1);
        expect(component.events.length).toBe(10);
    });

    it('should load current user and favorite events', () => {
        expect(userServiceMock.getCurrentUser).toHaveBeenCalled();
        expect(component.currentUser?.username).toBe('testuser');
        expect(component.isFavorite(1)).toBe(true);
    });

    it('should set hasMore false when events are less than size', () => {
        eventServiceMock.fetchEvents = vi.fn().mockReturnValue(of(mockEvents9));

        fixture = TestBed.createComponent(AllEventsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();

        expect(component.events.length).toBe(9);
        expect(component.hasMore).toBe(false);
    });

    it('should keep hasMore true when events length equals size', () => {
        expect(component.events.length).toBe(10);
        expect(component.hasMore).toBe(true);
    });

    it('should search events', () => {
        component.titleFilter = 'Music';
        component.categoryFilter = 'Music';
        component.minPriceFilter = '20';
        component.maxPriceFilter = '100';

        component.onSearch('Music');

        expect(eventServiceMock.fetchEvents).toHaveBeenLastCalledWith({
            participantId: null,
            title: 'Music',
            category: 'Music',
            minPrice: 20,
            maxPrice: 100,
            sortBy: undefined,
            sortDir: 'desc',
            page: 0,
            size: 10
        });
    });

    it('should clear filters', () => {
        component.titleFilter = 'Music';
        component.categoryFilter = 'Music';
        component.minPriceFilter = '20';
        component.maxPriceFilter = '100';
        component.sortBy = 'price';
        component.sortDir = 'asc';

        component.clearFilters();

        expect(component.titleFilter).toBe('');
        expect(component.categoryFilter).toBe('');
        expect(component.minPriceFilter).toBe('');
        expect(component.maxPriceFilter).toBe('');
        expect(component.sortBy).toBeNull();
        expect(component.sortDir).toBe('desc');
    });

    it('should change sort', () => {
        component.changeSort('price');

        expect(component.sortBy).toBe('price');
        expect(eventServiceMock.fetchEvents).toHaveBeenCalled();
    });

    it('should toggle sort direction', () => {
        component.sortDir = 'desc';

        component.toggleSortDir();

        expect(component.sortDir).toBe('asc');
    });

    it('should add favorite when event is not favorite', () => {
        component.favoriteEventIds.clear();

        component.toggleFavorite(2);

        expect(userServiceMock.addFavoriteEvent).toHaveBeenCalledWith(1, 2);
        expect(component.isFavorite(2)).toBe(true);
    });

    it('should remove favorite when event is already favorite', () => {
        component.favoriteEventIds.add(1);

        component.toggleFavorite(1);

        expect(userServiceMock.removeFavoriteEvent).toHaveBeenCalledWith(1, 1);
        expect(component.isFavorite(1)).toBe(false);
    });

    it('should not toggle favorite if user is not logged in', () => {
        component.currentUser = null;

        component.toggleFavorite(1);

        expect(userServiceMock.addFavoriteEvent).not.toHaveBeenCalled();
        expect(userServiceMock.removeFavoriteEvent).not.toHaveBeenCalled();
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
