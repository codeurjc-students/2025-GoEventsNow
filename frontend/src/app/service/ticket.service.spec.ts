import { describe, expect, it } from 'vitest';
import { API_BASE_URL, getAdminCookie } from './api-test-base';

const API_URL = API_BASE_URL + '/tickets/';

function buildTicket(ticketType: string, price: number, numTickets: number, eventId = 1) {
    return {
        ticketType,
        price,
        numTickets,
        eventId,
        userOwnerId: 1
    };
}

describe('Ticket API Integration', () => {

    it('should get tickets from real backend', async () => {
        const cookie = await getAdminCookie();

        const response = await fetch(API_URL + '?page=0&size=5', {
            headers: {
                Cookie: cookie
            }
        });

        expect(response.ok).toBe(true);

        const page = await response.json();

        expect(page.content).toBeDefined();
    });

    it('should not get tickets without authentication from real backend', async () => {
        const response = await fetch(API_URL + '?page=0&size=5', {
        });

        expect(response.status).toBe(401);
    });

    it('should create basic ticket in real backend', async () => {
        const cookie = await getAdminCookie();
        const newTicket = buildTicket('BASIC', 50.0, 1);

        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(newTicket)
        });

        expect(response.status).toBe(201);

        const createdTicket = await response.json();

        expect(createdTicket.id).toBeDefined();
        expect(createdTicket.ticketType).toBe(newTicket.ticketType);
        expect(createdTicket.numTickets).toBe(newTicket.numTickets);
        expect(createdTicket.eventId).toBe(newTicket.eventId);
    });

    it('should create vip ticket in real backend', async () => {
        const cookie = await getAdminCookie();
        const newTicket = buildTicket('VIP', 120.0, 1);

        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(newTicket)
        });

        expect(response.status).toBe(201);

        const createdTicket = await response.json();

        expect(createdTicket.id).toBeDefined();
        expect(createdTicket.ticketType).toBe(newTicket.ticketType);
    });

    it('should get ticket by id from real backend', async () => {
        const cookie = await getAdminCookie();
        const newTicket = buildTicket('BASIC', 50.0, 1);

        const createResponse = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(newTicket)
        });

        expect(createResponse.status).toBe(201);

        const createdTicket = await createResponse.json();

        const response = await fetch(API_URL + createdTicket.id, {
            headers: {
                Cookie: cookie
            }
        });

        expect(response.ok).toBe(true);

        const ticket = await response.json();

        expect(ticket.id).toBe(createdTicket.id);
        expect(ticket.ticketType).toBe(newTicket.ticketType);
    });

    it('should not create ticket with invalid event in real backend', async () => {
        const cookie = await getAdminCookie();
        const newTicket = buildTicket('BASIC', 50.0, 1, 999);

        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(newTicket)
        });

        expect(response.status).toBe(404);
    });

    it('should not create ticket with invalid type in real backend', async () => {
        const cookie = await getAdminCookie();
        const newTicket = buildTicket('GOLD', 50.0, 1);

        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(newTicket)
        });

        expect(response.status).toBe(400);
    });

});
