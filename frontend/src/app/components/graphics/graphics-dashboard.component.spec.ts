import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
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

  it('should build category legend with default color if backgroundColor is missing', () => {
    component.categoriesChart = {
      labels: ['Music'],
      data: [],
      backgroundColor: []
    };

    expect(component.categoryLegend).toEqual([
      { label: 'Music', value: 0, color: '#6c757d' }
    ]);
  });

  it('should render dashboard texts in the DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Ticket Statistics & Charts');
    expect(compiled.textContent).toContain('Tickets sold by event');
    expect(compiled.textContent).toContain('Tickets sold by category');
  });

  it('should handle service errors during initialization', () => {
    graphicServiceMock.getTicketsSoldByEvent.mockReturnValue(throwError(() => new Error('Error')));
    graphicServiceMock.getTicketsSoldByCategory.mockReturnValue(throwError(() => new Error('Error')));

    const errorFixture = TestBed.createComponent(GraphicsDashboardComponent);
    const errorComponent = errorFixture.componentInstance;
    errorFixture.detectChanges();

    expect(errorComponent.loading).toBe(false);
    expect(errorComponent.eventsChart.labels.length).toBe(0);
    expect(errorComponent.categoriesChart.labels.length).toBe(0);
  });

  it('should get bar max value', () => {
    expect(component.barMax).toBe(2);
  });

  it('should generate accurate conic gradient percentages when categoriesChart has data', () => {
    component.categoriesChart = {
      labels: ['Music', 'Tech'],
      data: [10, 10],
      backgroundColor: ['#FF5733', '#33FF57']
    };

    const gradient = component.pieGradient;

    expect(gradient).toContain('conic-gradient');
    expect(gradient).toContain('#FF5733 0deg 180deg');
    expect(gradient).toContain('#33FF57 180deg 360deg');
  });

  it('should return early in renderEventsChart if labels or canvas are missing', () => {
    (component as any).eventsChartInstance = undefined;
    component.eventsChart = { labels: [], data: [], backgroundColor: [] };
    component.eventsChartCanvas = { nativeElement: document.createElement('canvas') };

    (component as any).renderEventsChart();
    expect((component as any).eventsChartInstance).toBeUndefined();

    component.eventsChart = { labels: ['Event'], data: [10], backgroundColor: ['#000'] };
    component.eventsChartCanvas = undefined;

    (component as any).renderEventsChart();
    expect((component as any).eventsChartInstance).toBeUndefined();
  });

  it('should return early in renderCategoriesChart if labels or canvas are missing', () => {
    (component as any).categoriesChartInstance = undefined;
    component.categoriesChart = { labels: [], data: [], backgroundColor: [] };
    component.categoriesChartCanvas = { nativeElement: document.createElement('canvas') };

    (component as any).renderCategoriesChart();
    expect((component as any).categoriesChartInstance).toBeUndefined();

    component.categoriesChart = { labels: ['Category'], data: [5], backgroundColor: ['#000'] };
    component.categoriesChartCanvas = undefined;

    (component as any).renderCategoriesChart();
    expect((component as any).categoriesChartInstance).toBeUndefined();
  });

});