import { Participant } from "./participant";
import { Ticket } from "./ticket";

export interface Event {

    id?:number,
    title:string,
    category:string,
    location:string,
    date:string,
    image?:boolean,
    participants?: Participant[],
    tickets?: Ticket[]
    
}