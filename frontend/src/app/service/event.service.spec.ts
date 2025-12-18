import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { EventService } from "./event.service"
import { provideHttpClient } from "@angular/common/http";
import { TestBed } from "@angular/core/testing";
import { Event } from '../model/event';

describe ('APIService', () => {
    let eventService: EventService;
    let httpTesting: HttpTestingController;
    const baseUrl="/api/v1/events/";

    beforeEach ( () => {
        TestBed.configureTestingModule ({
            providers: [provideHttpClient(), provideHttpClientTesting()]
        });
        eventService = TestBed.inject(EventService);
        httpTesting= TestBed.inject(HttpTestingController);
    });

    
    afterEach ( () => {
        httpTesting.verify();
    });
    
    it('should get all events', () => {
        const mockEvents: Event[] = [
                {
                    "id": 1,
                    "title": "Taller de Spring Boot 4.0",
                    "category": "Tecnología",
                    "location": "Fuenlabrada, Madrid",
                    "date": "15-03-2026"
                },
                {
                    "id": 2,
                    "title": "Exposición Arte",
                    "category": "Cultura",
                    "location": "Barcelona",
                    "date": "12-02-2026"
                },
                {
                    "id": 3,
                    "title": "Torneo de Baloncesto",
                    "category": "Deportes",
                    "location": "Getafe, Madrid",
                    "date": "02-08-2026"
                }
        ];

        eventService.findAll().subscribe( (events:Event[]) => {
                expect(events.length).toBe(3);
                expect(events).toEqual(mockEvents);

        })

        const req = httpTesting.expectOne(baseUrl);
        expect(req.request.method).toBe('GET');
        req.flush(mockEvents);
    });

}) 