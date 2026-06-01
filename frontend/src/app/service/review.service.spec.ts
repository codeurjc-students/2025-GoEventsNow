import { describe, expect, it } from 'vitest';
import { API_BASE_URL, getAdminCookie } from './api-test-base';

const API_URL = API_BASE_URL + '/reviews/';

function buildReview(eventAssociatedId: number, rating: number, description: string) {
    return {
        description,
        rating,
        eventAssociatedId,
        userOwnerId: 2,
        createdAt: Date.now()
    }
}

describe('Review API Integration', () => {

    it('should get reviews by event id from real backend', async () => {
        const response = await fetch(API_URL + 'event/1');
        expect(response.ok).toBe(true);
        const page = await response.json();
        const reviews = page.content;
        expect(Array.isArray(reviews)).toBe(true);
    });

    it('should get reviews by username from real backend', async () => {
        const response = await fetch(API_URL + 'user/admin');
        expect(response.ok).toBe(true);
        const page = await response.json();
        const reviews = page.content;
        expect(Array.isArray(reviews)).toBe(true);
    });

    it('should create review by event id from real backend', async () => {
        const cookie = await getAdminCookie();
        const response = await fetch(API_URL + 'event/1', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(buildReview(1, 5, 'Great event'))
        });

        expect(response.ok).toBe(true);
        const review = await response.json();

        expect(review.description).toBe('Great event');
        expect(review.rating).toBe(5);
    });

    it('should not create review without authentication from real backend', async () => {
        const response = await fetch(API_URL + 'event/1', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(buildReview(1, 5, 'Great event'))
        });

        expect(response.status).toBe(401);
    });

    it('should replace review in real backend', async () => {
        const cookie = await getAdminCookie();
        const createResponse = await fetch(API_URL + 'event/1', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(buildReview(1, 4, 'Review to update'))
        });

        expect(createResponse.ok).toBe(true);

        const createdReview = await createResponse.json();

        const updateResponse = await fetch(API_URL + createdReview.id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(buildReview(1, 3, 'Updated review'))
        });

        expect(updateResponse.ok).toBe(true);

        const updatedReview = await updateResponse.json();

        expect(updatedReview.id).toBe(createdReview.id);
        expect(updatedReview.description).toBe('Updated review');
        expect(updatedReview.rating).toBe(3);
    });

    it('should delete review in real backend', async () => {
        const cookie = await getAdminCookie();

        const createResponse = await fetch(API_URL + 'event/1', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(buildReview(1, 2, 'Review to delete'))
        });

        expect(createResponse.ok).toBe(true);

        const createdReview = await createResponse.json();

        const deleteResponse = await fetch(API_URL + createdReview.id, {
            method: 'DELETE',
            headers: {
                Cookie: cookie
            }
        });

        expect(deleteResponse.ok).toBe(true);
    });

});
