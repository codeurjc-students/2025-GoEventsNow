import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ManageEventsComponent } from './manage-events.component';
import { EventService } from '../../service/event.service';
import { Router, provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';

const mockEvents = [
  { id: 1, title: 'Global Latin Music Festival' },
  { id: 2, title: 'Olivia Rodrigo Concert Experience' }
];

describe('ManageEventsComponent', () => {
  let component: ManageEventsComponent;
  let fixture: ComponentFixture<ManageEventsComponent>;
  let eventServiceMock: any;

  let navigateSpy: any;

beforeEach(async () => {
  eventServiceMock = {
    findAll: vi.fn().mockReturnValue(of(mockEvents)),
    deleteById: vi.fn().mockReturnValue(of({}))
  };

  await TestBed.configureTestingModule({
    imports: [ManageEventsComponent],
    providers: [
      provideRouter([]),
      { provide: EventService, useValue: eventServiceMock }
    ]
  }).compileComponents();

  fixture = TestBed.createComponent(ManageEventsComponent);
  component = fixture.componentInstance;

  const router = TestBed.inject(Router);
  navigateSpy = vi.spyOn(router, 'navigate');

  fixture.detectChanges();
});

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should load events on init', () => {
    expect(eventServiceMock.findAll).toHaveBeenCalledWith(0, 10);
    expect(component.events).toEqual(mockEvents);
    expect(component.page).toBe(1);
  });

  it('should set hasMore false when returned events are less than size', () => {
    expect(component.hasMore).toBe(false);
  });

  it('should not load events if hasMore is false', () => {
    vi.clearAllMocks();

    component.hasMore = false;
    component.loadEvents();

    expect(eventServiceMock.findAll).not.toHaveBeenCalled();
  });

  it('should keep hasMore true when returned events length equals size', () => {
    const tenEvents = Array.from({ length: 10 }, (_, i) => ({
      id: i + 1,
      title: `Event ${i + 1}`
    }));

    eventServiceMock.findAll = vi.fn().mockReturnValue(of(tenEvents));

    fixture = TestBed.createComponent(ManageEventsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component.events.length).toBe(10);
    expect(component.page).toBe(1);
    expect(component.hasMore).toBe(true);
  });

  it('should delete event and remove it from list', () => {
    component.events = [...mockEvents];

    component.deleteEvent(1);

    expect(eventServiceMock.deleteById).toHaveBeenCalledWith(1);
    expect(component.events.length).toBe(1);
    expect(component.events[0].id).toBe(2);
    expect(navigateSpy).toHaveBeenCalledWith(['/manage-events']);
  });

  it('should show alert when delete fails', () => {
    const alertSpy = vi.spyOn(window, 'alert').mockImplementation(() => {});
    eventServiceMock.deleteById = vi.fn().mockReturnValue(throwError(() => new Error('Delete failed')));

    component.deleteEvent(1);

    expect(eventServiceMock.deleteById).toHaveBeenCalledWith(1);
    expect(alertSpy).toHaveBeenCalledWith('Failed to delete event with id 1. Please try again later.');

    alertSpy.mockRestore();
  });

  it('should navigate to edit event page', () => {
    component.editEvent(1);

    expect(navigateSpy).toHaveBeenCalledWith(['/edit-event/1']);
  });

  it('should render event titles in DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Global Latin Music Festival');
    expect(compiled.textContent).toContain('Olivia Rodrigo Concert Experience');
  });
});