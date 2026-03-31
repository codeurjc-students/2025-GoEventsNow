
export interface Ticket {
    id?:number,
    ticketType:string,
    price:number,
    numTickets:number,
    eventId:number
    userOwnerId?:number
}