import { describe, expect, it } from 'vitest';
import { API_BASE_URL, getAdminCookie } from './api-test-base';

const API_URL = API_BASE_URL + '/events/';

function buildEvent(title: string) {
    return {
        title,
        description: 'This is a test event.',
        category: 'Testing',
        location: 'Test Location',
        date: '2026-12-31',
        time: '23:59',
        basicPrice: 10.0,
        vipPrice: 30.0,
        availableBasicTickets: 100,
        availableVipTickets: 20,
        image: false,
        participants: [
            {
                id: 1,
                name: 'Bad Bunny',
                type: 'Music Artist',
                biography: 'Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.',
                participantImage: true
            }
        ],
        tickets: []
    };
}

describe('Event API Integration', () => {

    it('should fetch events from real backend', async () => {
        const response = await fetch(API_URL + '?page=0&size=5', {
        });

        expect(response.ok).toBe(true);

        const page = await response.json();
        const events = page.content;

        expect(events.length).toBe(5);
        expect(events[0].title).toBe("Global Latin Music Festival");
        expect(events[0].description).toBe("A large-scale live music festival bringing together leading Latin and international artists. The event includes full-stage performances, VIP fan zones, immersive lighting, food areas and a night focused on reggaeton, pop and flamenco fusion.");
        expect(events[1].title).toBe("Olivia Rodrigo Concert Experience");
    });

    it('should get event by id from real backend', async () => {
        const response = await fetch(API_URL + '1', {
        });

        const event = await response.json();
        expect(response.ok).toBe(true);
        expect(event.title).toBe("Global Latin Music Festival");
        expect(event.description).toBe("A large-scale live music festival bringing together leading Latin and international artists. The event includes full-stage performances, VIP fan zones, immersive lighting, food areas and a night focused on reggaeton, pop and flamenco fusion.");
    });

    it('should fetch events by participant id from real backend', async () => {
        const response = await fetch(API_URL + '?participantId=1&page=0&size=5', {
        });

        expect(response.ok).toBe(true);

        const page = await response.json();
        const events = page.content;
        expect(events.length).toBeGreaterThan(0);
        expect(events[0].title).toBe("Global Latin Music Festival");
        expect(events[0].participants[0].name).toBe("Bad Bunny");
    });

    it('should fetch events with category filter from real backend', async () => {
        const response = await fetch(API_URL + '?category=Music&page=0&size=5', {
        });

        expect(response.ok).toBe(true);

        const page = await response.json();
        const events = page.content;

        expect(events.length).toBeGreaterThan(0);
        expect(events[0].category).toBe('Music');
    });

    it('should fetch events with min and max price filter from real backend', async () => {
        const response = await fetch(API_URL + '?minPrice=40&maxPrice=100&page=0&size=5', {
        });

        expect(response.ok).toBe(true);

        const page = await response.json();
        const events = page.content;

        expect(events.length).toBeGreaterThan(0);
        expect(events[0].category).toBe('Music');
    });

    it('should create event in real backend', async () => {
        const cookie = await getAdminCookie();
        const newEvent = buildEvent('Test Event' + Date.now());

        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(newEvent)
        });

        expect(response.status).toBe(201);

        const createdEvent = await response.json();

        expect(createdEvent.id).toBeDefined();
        expect(createdEvent.title).toBe(newEvent.title);
        expect(createdEvent.description).toBe(newEvent.description);
        expect(createdEvent.category).toBe(newEvent.category);
        expect(createdEvent.location).toBe(newEvent.location);
    });

    it('should replace event in real backend', async () => {
        const cookie = await getAdminCookie();

        const newEvent = buildEvent('Event To Update ' + Date.now());

        const createResponse = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(newEvent)
        });

        expect(createResponse.status).toBe(201);

        const createdEvent = await createResponse.json();

        const updatedEvent = buildEvent('Updated Event ' + Date.now());

        const response = await fetch(API_URL + createdEvent.id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(updatedEvent)
        });

        expect(response.ok).toBe(true);

        const event = await response.json();

        expect(event.title).toBe(updatedEvent.title);

    });

    it('should delete created event in real backend', async () => {
        const cookie = await getAdminCookie();

        const newEvent = buildEvent('Event To Delete ' + Date.now());

        const createResponse = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(newEvent)
        });

        expect(createResponse.status).toBe(201);

        const createdEvent = await createResponse.json();

        const deleteResponse = await fetch(API_URL + createdEvent.id, {
            method: 'DELETE',
            headers: {
                Cookie: cookie
            }
        });

        expect(deleteResponse.ok).toBe(true);

        const responseAfterDelete = await fetch(API_URL + createdEvent.id);

        expect(responseAfterDelete.status).toBe(404);
    });


});
