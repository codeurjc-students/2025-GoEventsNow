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


@Component({
    standalone: true,
    selector: 'app-ticket-selection',
    templateUrl: './ticket-selection.component.html',
    imports: [CommonModule, NgbAlert]
})

export class TicketSelectionComponent implements OnInit {

    quantity: number = 0;
    event$: Observable<Event> = new Observable<Event>();
    eventId: number = 0;
    user: User = {} as User;
    ticket: Ticket = {} as Ticket;
    ticketUser: Ticket[] = [];
    priceBasic: number = 89.99;
    priceTotal: number = 0;
    errorMessage: string | null = null;

    constructor(private ticketService: TicketService, private eventService: EventService, private activatedRoute: ActivatedRoute, private userService: UserService, private router: Router) {
    }

    ngOnInit(): void {
        const id = this.activatedRoute.snapshot.params['id'];
        this.event$ = this.eventService.findById(id);
        
        if (id) {
            this.eventId = Number(id);
            this.ticket = {
                ticketType: '',
                price: this.priceBasic,
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

        this.errorMessage = null;

        this.ticket.eventId = this.eventId
        this.ticket.userOwnerId = this.user.id
        this.ticket.numTickets = this.quantity

        this.reloadPriceTotal();

        this.ticket.price = this.priceTotal;

        this.ticketService.createTicket(this.ticket).subscribe({
            next: (ticket: Ticket) => {
                this.ticket = ticket
                this.user.tickets?.push(ticket)
                this.user.numTicketsBought = this.user.numTicketsBought + ticket.numTickets

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
        this.priceTotal = this.priceBasic * this.quantity;
    }
}