import { Injectable } from "@angular/core";
import { Ticket } from "../model/ticket";
import { HttpClient } from "@angular/common/http";
import { map, Observable } from "rxjs";


const BASE_URL = '/api/v1/tickets/';

@Injectable({
    providedIn: 'root'
})
export class TicketService {

     constructor(private readonly httpClient: HttpClient) { }

    public findAll(page = 0, size = 10): Observable<Ticket[]> {
        const url = `${BASE_URL}?page=${page}&size=${size}`;
        return this.httpClient.get<any>(url).pipe(
            map(response => response.content)
        );
    }

    public findById(ticketId: number | string): Observable<Ticket> {
        const id = Number(ticketId);
        return this.httpClient.get<Ticket>(BASE_URL + id);
    }

    public createTicket(ticket: Ticket): Observable<Ticket> {
        return this.httpClient.post<Ticket>(BASE_URL, ticket);
    }


}