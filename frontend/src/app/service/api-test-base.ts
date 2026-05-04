import { expect } from 'vitest';

export const API_BASE_URL = 'https://localhost:8443/api/v1';
export const AUTH_URL = API_BASE_URL + '/auth/';

export async function login(username = 'admin', password = 'adminpass'): Promise<Response> {
    const response = await fetch(AUTH_URL + 'login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username,
            password
        })
    });

    expect(response.ok).toBe(true);

    return response;
}

export function getCookieFromResponse(response: Response, cookieName = 'AuthToken'): string {
    const setCookie = response.headers.get('set-cookie');
    expect(setCookie).toBeTruthy();

    return setCookie!
        .split(',')
        .find(cookie => cookie.includes(cookieName))!
        .split(';')[0];
}

export async function getCookie(username = 'admin', password = 'adminpass', cookieName = 'AuthToken'): Promise<string> {
    return getCookieFromResponse(await login(username, password), cookieName);
}

export async function getAdminCookie(): Promise<string> {
    return getCookie();
}

export function buildRegisterRequest(username: string, email: string, phone = '123456789') {
    const formData = new URLSearchParams();

    formData.set('username', username);
    formData.set('fullname', 'Test User');
    formData.set('email', email);
    formData.set('password', 'newpass');
    formData.set('phone', phone);

    return formData;
}

export async function registerUser(username: string): Promise<void> {
    const registerRequest = buildRegisterRequest(username, username + '@email.com');

    const response = await fetch(AUTH_URL + 'register', {
        method: 'POST',
        body: registerRequest
    });

    expect(response.status).toBe(201);
}
