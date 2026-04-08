import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { EventService } from "../../service/event.service";
import { ActivatedRoute, Router } from "@angular/router";
import { Event } from "../../model/event";
import { User } from "../../model/user";
import { UserService } from "../../service/user.service";
import { Ticket } from "../../model/ticket";
import { TicketService } from "../../service/ticket.service";
import { NgbAlert } from "@ng-bootstrap/ng-bootstrap/alert";
import { FormsModule } from "@angular/forms";

@Component({
    standalone: true,
    selector: 'app-ticket-selection',
    templateUrl: './ticket-selection.component.html',
    imports: [CommonModule, NgbAlert, FormsModule]
})

export class TicketSelectionComponent implements OnInit {


    selectedTicketType: string = 'BASIC';
    quantity: number = 0;
    event$: Observable<Event> = new Observable<Event>();
    eventId: number = 0;
    user: User = {} as User;
    ticket: Ticket = {} as Ticket;
    ticketUser: Ticket[] = [];
    priceBasic: number = 0;
    priceVip: number = 0;
    basicAvailable: number = 0;
    vipAvailable: number = 0;
    priceTotal: number = 0;
    errorMessage: string | null = null;

    constructor(private ticketService: TicketService, private eventService: EventService, private activatedRoute: ActivatedRoute, private userService: UserService, private router: Router) {
    }

    ngOnInit(): void {
        const id = this.activatedRoute.snapshot.params['id'];
        this.event$ = this.eventService.findById(id);

        if (id) {
            this.eventId = Number(id);
            this.event$.subscribe(event => {
                this.priceBasic = event.basicPrice;
                this.priceVip = event.vipPrice;
                this.basicAvailable = event.availableBasicTickets;
                this.vipAvailable = event.availableVipTickets;
            });
            this.ticket = {
                ticketType: '',
                price: 0,
                numTickets: 0,
                eventId: 0,
                userOwnerId: 0
            }

            this.userService.getCurrentUser().subscribe(user => {
                this.user = user;
                this.ticket.userOwnerId = user.id || 0;
            });
        }

    }


    increase(): void {
        this.quantity += 1;
        this.reloadPriceTotal();
    }

    decrease(): void {
        if (this.quantity > 0) {
            this.quantity -= 1;
            this.reloadPriceTotal();
        }
    }

    pay(): void {

        if (this.quantity <= 0) {
            this.errorMessage = 'Please select at least one ticket.';
            return;
        }

        if (this.selectedTicketType === 'BASIC' && this.quantity > this.basicAvailable) {
            this.errorMessage = 'Not enough basic tickets available.';
            return;
        }

        if (this.selectedTicketType === 'VIP' && this.quantity > this.vipAvailable) {
            this.errorMessage = 'Not enough VIP tickets available.';
            return;
        }


        this.errorMessage = null;

        this.ticket.eventId = this.eventId;
        this.ticket.userOwnerId = this.user.id;
        this.ticket.numTickets = this.quantity;
        this.ticket.ticketType = this.selectedTicketType;

        this.reloadPriceTotal();

        this.ticket.price = this.priceTotal;

        this.ticketService.createTicket(this.ticket).subscribe({
            next: (ticket: Ticket) => {
                this.ticket = ticket
                this.user.tickets?.push(ticket)
                this.user.numTicketsBought = this.user.numTicketsBought + ticket.numTickets
                this.event$.subscribe(event => {
                    if (ticket.ticketType === 'BASIC') {
                        event.availableBasicTickets = event.availableBasicTickets - ticket.numTickets;
                    } else if (ticket.ticketType === 'VIP') {
                        event.availableVipTickets = event.availableVipTickets - ticket.numTickets;
                    }
                    this.eventService.createOrReplaceEvent(event).subscribe({
                        next: () => {
                            this.event$ = this.eventService.findById(this.eventId);
                        }
                    });
                });

                this.userService.replaceUser(this.user).subscribe({
                    next: (updatedUser: User) => {
                        this.user = updatedUser;
                        this.router.navigate(['/user/' + this.user.id]);
                    }
                });
            }
        });

    }

    reloadPriceTotal(): void {
        if (this.selectedTicketType === 'BASIC') {
            this.priceTotal = this.priceBasic * this.quantity;
        } else {
            this.priceTotal = this.priceVip * this.quantity;
        }
    }
}