
import { EventService } from "./event.service"
import { provideHttpClient } from "@angular/common/http";
import { TestBed } from "@angular/core/testing";
import { firstValueFrom } from "rxjs";

describe('APIService', () => {
    let eventService: EventService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [provideHttpClient()]
        });
        eventService = TestBed.inject(EventService);

    });


    it('should get events from real backend', async () => {
        const events = await firstValueFrom(eventService.findAll());

        expect(events).toBeTruthy();
        expect(events.length).toBeGreaterThan(0);
        expect(events[0].title).toBe("Taller de Spring Boot 4.0");
    });

}) 