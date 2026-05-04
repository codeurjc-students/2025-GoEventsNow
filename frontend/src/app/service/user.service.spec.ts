import { describe, expect, it } from 'vitest';
import { API_BASE_URL, getAdminCookie, getCookie, registerUser } from './api-test-base';

const API_URL = API_BASE_URL + '/users/';

async function getCurrentUser(cookie: string) {
    const response = await fetch(API_URL + 'me', {
        headers: {
            Cookie: cookie
        }
    });

    expect(response.ok).toBe(true);

    return response.json();
}

async function userExists(username: string) {
    const response = await fetch(API_URL + 'exists?username=' + encodeURIComponent(username));

    expect(response.ok).toBe(true);

    return response.json();
}

function buildUser(fullname: string, email: string) {
    return {
        fullname,
        phone: 123456789,
        email
    };
}

describe('User API Integration', () => {

    it('should get current user from real backend', async () => {
        const cookie = await getAdminCookie();
        const user = await getCurrentUser(cookie);

        expect(user.id).toBeDefined();
        expect(user.username).toBe('admin');
        expect(user.email).toBeTruthy();
    });

    it('should not get current user without authentication from real backend', async () => {
        const response = await fetch(API_URL + 'me');

        expect(response.status).toBe(401);
    });

    it('should replace current user in real backend', async () => {
        const cookie = await getAdminCookie();
        const currentUser = await getCurrentUser(cookie);
        const updatedUser = buildUser('Updated User ' + Date.now(), 'updated' + Date.now() + '@email.com');

        const response = await fetch(API_URL + currentUser.id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                Cookie: cookie
            },
            body: JSON.stringify(updatedUser)
        });

        expect(response.ok).toBe(true);

        const user = await response.json();
        expect(user.fullname).toBe(updatedUser.fullname);
        expect(user.phone).toBe(updatedUser.phone);
        expect(user.email).toBe(updatedUser.email);
        expect(user.username).toBe(currentUser.username);
    });

    it('should detect existing username in real backend', async () => {
        const exists = await userExists('admin');

        expect(exists).toBe(true);
    });

});
