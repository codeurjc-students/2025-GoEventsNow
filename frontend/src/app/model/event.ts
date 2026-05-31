import { Participant } from "./participant";
import { Review } from "./review";
import { Ticket } from "./ticket";

export interface Event {

    id?:number,
    title:string,
    description:string,
    category:string,
    location:string,
    date:string,
    time:string,
    basicPrice:number,
    vipPrice:number,
    availableBasicTickets:number,
    availableVipTickets:number,
    image?:boolean,
    participants?: Participant[],
    tickets?: Ticket[],
    reviews?: Review[]
    
}