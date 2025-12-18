
import { Component, OnInit } from '@angular/core'
import { EventService } from './service/event.service';
import { Event } from './model/event';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';

@Component ({
    standalone: true,
    selector: 'app-root',
    templateUrl: './app.component.html',
    imports: [CommonModule]
})

export class AppComponent implements OnInit {


    events$: Observable<Event[]> = new Observable<Event[]> ();
    constructor (private eventService:EventService){}

    ngOnInit():void{
         this.events$ = this.eventService.findAll();
    }

}