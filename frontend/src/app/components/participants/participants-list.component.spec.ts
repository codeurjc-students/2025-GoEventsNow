import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { ParticipantsListComponent } from './participants-list.component';
import { ParticipantService } from '../../service/participant.service';

const mockParticipants10 = [
  { id: 1, name: 'Bad Bunny', type: 'Music Artist', numFollowers: 100 },
  { id: 2, name: 'Olivia Rodrigo', type: 'Music Artist', numFollowers: 90 },
  { id: 3, name: 'Juan Dávila', type: 'Comedian', numFollowers: 80 },
  { id: 4, name: 'Rafael Nadal', type: 'Athlete', numFollowers: 70 },
  { id: 5, name: 'Elon Musk', type: 'Technology', numFollowers: 60 },
  { id: 6, name: 'Christopher Nolan', type: 'Cinema', numFollowers: 50 },
  { id: 7, name: 'Michael Jordan', type: 'Athlete', numFollowers: 40 },
  { id: 8, name: 'Jordi Roca', type: 'Chef', numFollowers: 30 },
  { id: 9, name: 'Pau Gasol', type: 'Athlete', numFollowers: 20 },
  { id: 10, name: 'Zendaya', type: 'Cinema', numFollowers: 10 }
];

const mockParticipants9 = mockParticipants10.slice(0, 9);

describe('ParticipantsListComponent', () => {
  let component: ParticipantsListComponent;
  let fixture: ComponentFixture<ParticipantsListComponent>;
  let participantServiceMock: any;

  beforeEach(async () => {
    participantServiceMock = {
      fetchParticipants: vi.fn().mockReturnValue(of(mockParticipants10))
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

  it('should load participants in ngOnInit', () => {
    expect(participantServiceMock.fetchParticipants).toHaveBeenCalledWith({
      page: 0,
      size: 10,
      name: undefined,
      types: [],
      sortBy: undefined,
      sortDir: 'desc'
    });

    expect(component.participants.length).toBe(10);
    expect(component.page).toBe(1);
    expect(component.hasMore).toBe(true);
  });

  it('should load available participant types', () => {
    expect(participantServiceMock.fetchParticipants).toHaveBeenCalledWith({ page: 0, size: 1000 });

    expect(component.types).toEqual([
      'Athlete',
      'Chef',
      'Cinema',
      'Comedian',
      'Music Artist',
      'Technology'
    ]);
  });

  it('should set hasMore false when participants are less than size', () => {
    participantServiceMock.fetchParticipants = vi.fn().mockReturnValue(of(mockParticipants9));

    fixture = TestBed.createComponent(ParticipantsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.participants.length).toBe(9);
    expect(component.hasMore).toBe(false);
  });

  it('should apply search filter', () => {
    component.onSearch('Bad Bunny');

    expect(component.searchTerm).toBe('Bad Bunny');
    expect(participantServiceMock.fetchParticipants).toHaveBeenLastCalledWith({
      page: 0,
      size: 10,
      name: 'Bad Bunny',
      types: [],
      sortBy: undefined,
      sortDir: 'desc'
    });
  });

  it('should apply category filter', () => {
    component.onCategoryChange('Music Artist');

    expect(component.categoryFilter).toBe('Music Artist');
    expect(component.selectedTypes).toEqual(['Music Artist']);
    expect(participantServiceMock.fetchParticipants).toHaveBeenLastCalledWith({
      page: 0,
      size: 10,
      name: undefined,
      types: ['Music Artist'],
      sortBy: undefined,
      sortDir: 'desc'
    });
  });

  it('should change sort', () => {
    component.changeSort('numFollowers');

    expect(component.sortBy).toBe('numFollowers');
    expect(participantServiceMock.fetchParticipants).toHaveBeenLastCalledWith({
      page: 0,
      size: 10,
      name: undefined,
      types: [],
      sortBy: 'numFollowers',
      sortDir: 'desc'
    });
  });

  it('should toggle sort direction', () => {
    component.toggleSortDir();

    expect(component.sortDir).toBe('asc');
    expect(participantServiceMock.fetchParticipants).toHaveBeenLastCalledWith({
      page: 0,
      size: 10,
      name: undefined,
      types: [],
      sortBy: undefined,
      sortDir: 'asc'
    });
  });

  it('should clear filters', () => {
    component.searchTerm = 'Bad';
    component.categoryFilter = 'Music Artist';
    component.selectedTypes = ['Music Artist'];
    component.sortBy = 'numFollowers';
    component.sortDir = 'asc';

    component.clearFilters();

    expect(component.searchTerm).toBe('');
    expect(component.categoryFilter).toBe('');
    expect(component.selectedTypes).toEqual([]);
    expect(component.sortBy).toBeNull();
    expect(component.sortDir).toBe('desc');
  });

  it('should render participant names in the DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Bad Bunny');
    expect(compiled.textContent).toContain('Olivia Rodrigo');
    expect(compiled.textContent).toContain('Juan Dávila');
  });

  it('should not load participants if hasMore is false', () => {
    vi.clearAllMocks();

    component.hasMore = false;
    component.loadParticipants();

    expect(participantServiceMock.fetchParticipants).not.toHaveBeenCalled();
  });

});
