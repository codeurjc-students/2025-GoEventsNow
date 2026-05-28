import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { GraphicsDashboardComponent } from './graphics-dashboard.component';
import { GraphicService } from '../../service/graphic.service';

const mockTicketsSoldByEvent = {
  labels: ['Global Latin Music Festival'],
  data: [2],
  backgroundColor: ['#4E79A7']
};

const mockTicketsSoldByCategory = {
  labels: ['Music'],
  data: [2],
  backgroundColor: ['#4E79A7']
};

describe('GraphicsDashboardComponent', () => {
  let component: GraphicsDashboardComponent;
  let fixture: ComponentFixture<GraphicsDashboardComponent>;
  let graphicServiceMock: any;

  beforeEach(async () => {
    graphicServiceMock = {
      getTicketsSoldByEvent: vi.fn().mockReturnValue(of(mockTicketsSoldByEvent)),
      getTicketsSoldByCategory: vi.fn().mockReturnValue(of(mockTicketsSoldByCategory))
    };

    await TestBed.configureTestingModule({
      imports: [GraphicsDashboardComponent],
      providers: [
        { provide: GraphicService, useValue: graphicServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(GraphicsDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should load tickets sold by event', () => {
    expect(graphicServiceMock.getTicketsSoldByEvent).toHaveBeenCalledTimes(1);
    expect(component.eventsChart).toEqual(mockTicketsSoldByEvent);
  });

  it('should load tickets sold by category', () => {
    expect(graphicServiceMock.getTicketsSoldByCategory).toHaveBeenCalledTimes(1);
    expect(component.categoriesChart).toEqual(mockTicketsSoldByCategory);
  });

  it('should calculate total tickets sold', () => {
    expect(component.totalTicketsSold).toBe(2);
  });

  it('should calculate total events', () => {
    expect(component.totalEvents).toBe(1);
  });

  it('should return top event label', () => {
    expect(component.topEventLabel).toBe('Global Latin Music Festival');
  });

  it('should return top category label', () => {
    expect(component.topCategoryLabel).toBe('Music');
  });

  it('should build category legend', () => {
    expect(component.categoryLegend).toEqual([
      { label: 'Music', value: 2, color: '#4E79A7' }
    ]);
  });

  it('should render dashboard texts in the DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Ticket Statistics & Charts');
    expect(compiled.textContent).toContain('Tickets sold by event');
    expect(compiled.textContent).toContain('Tickets sold by category');
  });
});