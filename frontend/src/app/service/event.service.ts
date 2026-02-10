import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Event } from '../model/event';

const BASE_URL = "http://localhost:8080/api/v1/events/"

@Injectable ({ providedIn: 'root'})
export class EventService {

    constructor (private httpClient: HttpClient){}

    public findAll():Observable<Event[]>{
        return this.httpClient.get<Event[]>(BASE_URL);
    }
}