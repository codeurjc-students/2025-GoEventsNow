import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnDestroy, ViewChild } from '@angular/core';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { GraphicResponse, GraphicService } from '../../service/graphic.service';

Chart.register(...registerables);

@Component({
  standalone: true,
  selector: 'app-graphics-dashboard',
  templateUrl: './graphics-dashboard.component.html',
  imports: [CommonModule]
})
export class GraphicsDashboardComponent implements AfterViewInit, OnDestroy {

  @ViewChild('eventsChartCanvas') eventsChartCanvas?: ElementRef<HTMLCanvasElement>;
  @ViewChild('categoriesChartCanvas') categoriesChartCanvas?: ElementRef<HTMLCanvasElement>;

  eventsChart: GraphicResponse = { labels: [], data: [], backgroundColor: [] };
  categoriesChart: GraphicResponse = { labels: [], data: [], backgroundColor: [] };
  loading = true;
  private eventsChartInstance?: Chart;
  private categoriesChartInstance?: Chart;
  private pendingEventsChart = false;
  private pendingCategoriesChart = false;

  constructor(private readonly graphicService: GraphicService, private readonly changeDetectorRef: ChangeDetectorRef) {}

  ngAfterViewInit(): void {
    this.graphicService.getTicketsSoldByEvent().subscribe({
      next: (response) => {
        this.eventsChart = response;
        this.pendingEventsChart = true;
        this.updateLoadingState();
        this.scheduleChartRender();
      },
      error: () => {
        this.pendingEventsChart = true;
        this.updateLoadingState();
      }
    });

    this.graphicService.getTicketsSoldByCategory().subscribe({
      next: (response) => {
        this.categoriesChart = response;
        this.pendingCategoriesChart = true;
        this.updateLoadingState();
        this.scheduleChartRender();
      },
      error: () => {
        this.pendingCategoriesChart = true;
        this.updateLoadingState();
      }
    });
  }

  ngOnDestroy(): void {
    this.eventsChartInstance?.destroy();
    this.categoriesChartInstance?.destroy();
  }

  get totalTicketsSold(): number {
    return this.eventsChart.data.reduce((sum, value) => sum + value, 0);
  }

  get totalEvents(): number {
    return this.eventsChart.labels.length;
  }

  get topEventLabel(): string {
    const index = this.eventsChart.data.indexOf(Math.max(...this.eventsChart.data, 0));
    return index >= 0 ? this.eventsChart.labels[index] : 'No data';
  }

  get topCategoryLabel(): string {
    const index = this.categoriesChart.data.indexOf(Math.max(...this.categoriesChart.data, 0));
    return index >= 0 ? this.categoriesChart.labels[index] : 'No data';
  }

  get barMax(): number {
    return Math.max(...this.eventsChart.data, 0);
  }

  get pieGradient(): string {
    if (!this.categoriesChart.data.length) {
      return 'conic-gradient(#e9ecef 0deg 360deg)';
    }

    const total = this.categoriesChart.data.reduce((sum, value) => sum + value, 0);
    let accumulated = 0;

    const slices = this.categoriesChart.data.map((value, index) => {
      const start = (accumulated / total) * 360;
      accumulated += value;
      const end = (accumulated / total) * 360;
      const color = this.categoriesChart.backgroundColor[index] || '#6c757d';
      return `${color} ${start}deg ${end}deg`;
    });

    return `conic-gradient(${slices.join(', ')})`;
  }

  get categoryLegend(): Array<{ label: string; value: number; color: string }> {
    return this.categoriesChart.labels.map((label, index) => ({
      label,
      value: this.categoriesChart.data[index] ?? 0,
      color: this.categoriesChart.backgroundColor[index] || '#6c757d'
    }));
  }

  private updateLoadingState(): void {
    this.loading = !(this.pendingEventsChart && this.pendingCategoriesChart);
  }

  private scheduleChartRender(): void {
    this.changeDetectorRef.detectChanges();
    queueMicrotask(() => this.renderChartsIfReady());
  }

  private renderChartsIfReady(): void {
    this.renderEventsChart();
    this.renderCategoriesChart();
  }

  private renderEventsChart(): void {
    this.eventsChartInstance?.destroy();

    if (!this.eventsChart.labels.length) {
      return;
    }

    const config: ChartConfiguration<'bar', number[], string> = {
      type: 'bar',
      data: {
        labels: this.eventsChart.labels,
        datasets: [{
          label: 'Tickets sold',
          data: this.eventsChart.data,
          backgroundColor: this.eventsChart.backgroundColor,
          borderRadius: 10,
          borderSkipped: false
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false }
        },
        scales: {
          x: {
            ticks: { color: '#6c757d' },
            grid: { display: false }
          },
          y: {
            beginAtZero: true,
            ticks: { color: '#6c757d', precision: 0 },
            grid: { color: 'rgba(108,117,125,0.12)' }
          }
        }
      }
    };

    const canvas = this.eventsChartCanvas?.nativeElement;
    if (!canvas) {
      return;
    }

    this.eventsChartInstance = new Chart(canvas, config);
  }

  private renderCategoriesChart(): void {
    this.categoriesChartInstance?.destroy();

    if (!this.categoriesChart.labels.length) {
      return;
    }

    const config: ChartConfiguration<'pie', number[], string> = {
      type: 'pie',
      data: {
        labels: this.categoriesChart.labels,
        datasets: [{
          data: this.categoriesChart.data,
          backgroundColor: this.categoriesChart.backgroundColor,
          borderColor: '#ffffff',
          borderWidth: 2
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          }
        }
      }
    };

    const canvas = this.categoriesChartCanvas?.nativeElement;
    if (!canvas) {
      return;
    }

    this.categoriesChartInstance = new Chart(canvas, config);
  }
}