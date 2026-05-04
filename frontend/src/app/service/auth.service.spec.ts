import { describe, expect, it } from 'vitest';
import { AUTH_URL, buildRegisterRequest, getCookieFromResponse, login } from './api-test-base';

describe('Auth API Integration', () => {

    it('should login user in real backend', async () => {
        const response = await login();

        expect(response.headers.get('set-cookie')).toContain('AuthToken');
        expect(response.headers.get('set-cookie')).toContain('RefreshToken');

        const authResponse = await response.json();
        expect(authResponse.status).toBe('SUCCESS');
    });

    it('should not login user with wrong password in real backend', async () => {
        const response = await fetch(AUTH_URL + 'login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: 'admin',
                password: 'wrongpass'
            })
        });

        expect(response.status).toBe(401);
    });

    it('should refresh auth token in real backend', async () => {
        const refreshToken = getCookieFromResponse(await login(), 'RefreshToken');

        const response = await fetch(AUTH_URL + 'refresh', {
            method: 'POST',
            headers: {
                Cookie: refreshToken
            }
        });

        expect(response.ok).toBe(true);
        expect(response.headers.get('set-cookie')).toContain('AuthToken');

        const authResponse = await response.json();
        expect(authResponse.status).toBe('SUCCESS');
    });

    it('should not refresh auth token without cookie in real backend', async () => {
        const response = await fetch(AUTH_URL + 'refresh', {
            method: 'POST'
        });

        expect(response.status).toBe(401);

        const authResponse = await response.json();
        expect(authResponse.status).toBe('FAILURE');
    });

    it('should logout user in real backend', async () => {
        const response = await fetch(AUTH_URL + 'logout', {
            method: 'POST'
        });

        expect(response.ok).toBe(true);

        const authResponse = await response.json();
        expect(authResponse.status).toBe('SUCCESS');
        expect(authResponse.message).toBe('logout successfully');
    });

    it('should register user in real backend', async () => {
        const timestamp = Date.now();
        const registerRequest = buildRegisterRequest('newuser' + timestamp, 'newuser' + timestamp + '@email.com');

        const response = await fetch(AUTH_URL + 'register', {
            method: 'POST',
            body: registerRequest
        });

        expect(response.status).toBe(201);
        expect(response.headers.get('location')).toContain('/api/v1/users/');

        const authResponse = await response.json();
        expect(authResponse.status).toBe('SUCCESS');
        expect(authResponse.message).toBe('User registered');
    });

    it('should not register user with invalid phone in real backend', async () => {
        const registerRequest = buildRegisterRequest('phoneuser' + Date.now(), 'phoneuser@email.com', 'notNumber');

        const response = await fetch(AUTH_URL + 'register', {
            method: 'POST',
            body: registerRequest
        });

        expect(response.status).toBe(400);

    });

});
