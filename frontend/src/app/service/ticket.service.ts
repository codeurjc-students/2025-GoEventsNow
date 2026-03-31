import { Injectable } from "@angular/core";
import { Ticket } from "../model/ticket";
import { Observable } from "rxjs/internal/Observable";
import { HttpClient } from "@angular/common/http";


const BASE_URL = '/api/v1/tickets/';

@Injectable({
    providedIn: 'root'
})
export class TicketService {

     constructor(private httpClient: HttpClient) { }

    public findAll(): Observable<Ticket[]> {
        return this.httpClient.get<Ticket[]>(BASE_URL);
    }

    public findById(ticketId: number | string): Observable<Ticket> {
        const id = Number(ticketId);
        return this.httpClient.get<Ticket>(BASE_URL + id);
    }

    public createTicket(ticket: Ticket): Observable<Ticket> {
        return this.httpClient.post<Ticket>(BASE_URL, ticket);
    }


}