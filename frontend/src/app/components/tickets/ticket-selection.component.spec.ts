import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom, of, throwError } from 'rxjs';
import { TicketSelectionComponent } from './ticket-selection.component';
import { TicketService } from '../../service/ticket.service';
import { EventService } from '../../service/event.service';
import { UserService } from '../../service/user.service';
import { Event } from '../../model/event';
import { Ticket } from '../../model/ticket';

const mockEvent: Event = {
  id: 1,
  title: 'Global Latin Music Festival',
  description: 'A large-scale live music festival bringing together leading Latin and international artists.',
  category: 'Music',
  location: 'Madrid, WiZink Center',
  date: '2026-05-10',
  time: '21:00',
  basicPrice: 50,
  vipPrice: 120,
  availableBasicTickets: 100,
  availableVipTickets: 20
};

const mockTicket: Ticket = {
  id: 1,
  ticketType: 'BASIC',
  price: 100,
  numTickets: 2,
  eventId: 1,
  userOwnerId: 5
};

describe('TicketSelectionComponent', () => {
  let component: TicketSelectionComponent;
  let fixture: ComponentFixture<TicketSelectionComponent>;
  let ticketServiceMock: any;
  let eventServiceMock: any;
  let navigateSpy: any;

  beforeEach(async () => {
    ticketServiceMock = {
      createTicket: vi.fn().mockReturnValue(of(mockTicket))
    };

    eventServiceMock = {
      findById: vi.fn().mockReturnValue(of(mockEvent))
    };

    await TestBed.configureTestingModule({
      imports: [TicketSelectionComponent],
      providers: [
        { provide: TicketService, useValue: ticketServiceMock },
        { provide: EventService, useValue: eventServiceMock },
        { provide: UserService, useValue: {} },
        { provide: ActivatedRoute, useValue: { snapshot: { params: { id: 1 } } } },
        { provide: Router, useValue: { navigate: vi.fn() } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TicketSelectionComponent);
    component = fixture.componentInstance;
    navigateSpy = TestBed.inject(Router).navigate;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should load event and prices on init', async () => {
    const event = await firstValueFrom(component.event$);

    expect(eventServiceMock.findById).toHaveBeenCalledWith(1);
    expect(component.eventId).toBe(1);
    expect(component.priceBasic).toBe(50);
    expect(component.priceVip).toBe(120);
    expect(component.basicAvailable).toBe(100);
    expect(component.vipAvailable).toBe(20);
    expect(event.title).toBe('Global Latin Music Festival');
  });

  it('should increase quantity and reload total price', () => {
    component.increase();

    expect(component.quantity).toBe(1);
    expect(component.priceTotal).toBe(50);
  });

  it('should decrease quantity and not go below zero', () => {
    component.decrease();

    expect(component.quantity).toBe(0);

    component.quantity = 2;
    component.decrease();

    expect(component.quantity).toBe(1);
    expect(component.priceTotal).toBe(50);
  });

  it('should calculate VIP total price', () => {
    component.selectedTicketType = 'VIP';
    component.quantity = 2;

    component.reloadPriceTotal();

    expect(component.priceTotal).toBe(240);
  });

  it('should not pay with zero tickets', () => {
    component.quantity = 0;

    component.pay();

    expect(component.errorMessage).toBe('Please select at least one ticket.');
    expect(ticketServiceMock.createTicket).not.toHaveBeenCalled();
  });

  it('should not pay when selected basic tickets exceed availability', () => {
    component.selectedTicketType = 'BASIC';
    component.quantity = 101;

    component.pay();

    expect(component.errorMessage).toBe('Not enough basic tickets available.');
    expect(ticketServiceMock.createTicket).not.toHaveBeenCalled();
  });

  it('should create ticket and navigate to user page when pay is valid', () => {
    component.selectedTicketType = 'BASIC';
    component.quantity = 2;

    component.pay();

    expect(ticketServiceMock.createTicket).toHaveBeenCalledWith({
      ticketType: 'BASIC',
      price: 100,
      numTickets: 2,
      eventId: 1
    });
    expect(navigateSpy).toHaveBeenCalledWith(['/user/5']);
  });

  it('should show error message when pay fails', () => {
    const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    ticketServiceMock.createTicket = vi.fn().mockReturnValue(
      throwError(() => ({
        error: {
          message: 'Purchase failed'
        }
      }))
    );
    component.quantity = 1;

    component.pay();

    expect(component.errorMessage).toBe('Purchase failed');
    consoleSpy.mockRestore();
  });

  it('should render event title in DOM', () => {
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Global Latin Music Festival');
    expect(compiled.textContent).toContain('Order Summary');
  });
});
