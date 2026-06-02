import { describe, expect, it } from 'vitest';
import { API_BASE_URL, getAdminCookie } from './api-test-base';

const API_URL = API_BASE_URL + '/participants/';

function buildParticipant(name: string) {
    return {
        name,
        type: 'Testing',
        biography: 'Test biography.',
        participantImage: false
    };
}

describe('Participant API Integration', () => {

    it('should fetch participants from real backend', async () => {
        const response = await fetch(API_URL + '?page=0&size=5', {
        });

        expect(response.ok).toBe(true);

        const page = await response.json();
        const participants = page.content;

        expect(participants.length).toBe(5);
        expect(participants[0].name).toBe("Bad Bunny");
    });

    it('should fetch participants with type filter from real backend', async () => {
        const response = await fetch(API_URL + '?types=Actor&page=0&size=5', {
        });

        expect(response.ok).toBe(true);

        const page = await response.json();
        const participants = page.content;

        expect(participants.length).toBeGreaterThan(0);
        expect(participants[0].type).toBe('Actor');
    });

    it('should fetch participants with name filter from real backend', async () => {
        const response = await fetch(API_URL + '?name=Bad&page=0&size=5', {
        });

        expect(response.ok).toBe(true);

        const page = await response.json();
        const participants = page.content;

        expect(participants.length).toBeGreaterThan(0);
        expect(participants[0].name).toBe('Bad Bunny');
    });

    it('should fetch participant order by recent desc from real backend', async () => {
        const response = await fetch(API_URL + '?sortBy=recent&sortDir=desc&page=0&size=5', {

        });

        expect(response.ok).toBe(true);

        const page = await response.json();
        const participants = page.content;

        expect(participants.length).toBeGreaterThan(0);
        expect(participants[0].name).toBeTruthy();
    });

    it('should get participant by id from real backend', async () => {
        const response = await fetch(API_URL + '1', {
        });

        const participant = await response.json();
        expect(response.ok).toBe(true);
        expect(participant.name).toBe("Bad Bunny");
    });

    it('should create participant in real backend', async () => {
        const cookie = await getAdminCookie();
        const newParticipant = buildParticipant('Test Participant' + Date.now());

        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(newParticipant)
        });

        expect(response.status).toBe(201);

        const createdParticipant = await response.json();

        expect(createdParticipant.id).toBeDefined();
        expect(createdParticipant.name).toBe(newParticipant.name);
        expect(createdParticipant.type).toBe(newParticipant.type);
        expect(createdParticipant.biography).toBe(newParticipant.biography);
    });

    it('should replace participant in real backend', async () => {
        const cookie = await getAdminCookie();

        const newParticipant = buildParticipant('Participant To Update ' + Date.now());

        const createResponse = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(newParticipant)
        });

        expect(createResponse.status).toBe(201);

        const createdParticipant = await createResponse.json();

        const updatedParticipant = buildParticipant('Updated Participant ' + Date.now());

        const response = await fetch(API_URL + createdParticipant.id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(updatedParticipant)
        });

        expect(response.ok).toBe(true);

        const participant = await response.json();

        expect(participant.name).toBe(updatedParticipant.name);
    });

    it('should delete created participant in real backend', async () => {
        const cookie = await getAdminCookie();

        const newParticipant = buildParticipant('Participant To Delete ' + Date.now());

        const createResponse = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(newParticipant)
        });

        expect(createResponse.status).toBe(201);

        const createdParticipant = await createResponse.json();

        const deleteResponse = await fetch(API_URL + createdParticipant.id, {
            method: 'DELETE',
            headers: {
                Cookie: cookie
            }
        });

        expect(deleteResponse.ok).toBe(true);

        const responseAfterDelete = await fetch(API_URL + createdParticipant.id);

        expect(responseAfterDelete.status).toBe(404);
    });

});
