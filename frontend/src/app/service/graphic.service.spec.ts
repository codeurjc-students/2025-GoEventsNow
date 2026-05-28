import { describe, expect, it } from 'vitest';
import { API_BASE_URL } from './api-test-base';

const API_URL = API_BASE_URL + '/graphics/';

describe('Graphic API Integration', () => {

  it('should get tickets sold by event from real backend', async () => {
    const response = await fetch(API_URL + 'bargraph');

    expect(response.ok).toBe(true);

    const graphic = await response.json();

    expect(graphic.labels).toBeDefined();
    expect(graphic.data).toBeDefined();
    expect(graphic.backgroundColor).toBeDefined();

    expect(Array.isArray(graphic.labels)).toBe(true);
    expect(Array.isArray(graphic.data)).toBe(true);
    expect(Array.isArray(graphic.backgroundColor)).toBe(true);

    expect(graphic.labels.length).toBe(graphic.data.length);
    expect(graphic.labels.length).toBe(graphic.backgroundColor.length);
  });

  it('should get tickets sold by category from real backend', async () => {
    const response = await fetch(API_URL + 'piechart');

    expect(response.ok).toBe(true);

    const graphic = await response.json();

    expect(graphic.labels).toBeDefined();
    expect(graphic.data).toBeDefined();
    expect(graphic.backgroundColor).toBeDefined();

    expect(Array.isArray(graphic.labels)).toBe(true);
    expect(Array.isArray(graphic.data)).toBe(true);
    expect(Array.isArray(graphic.backgroundColor)).toBe(true);

    expect(graphic.labels.length).toBe(graphic.data.length);
    expect(graphic.labels.length).toBe(graphic.backgroundColor.length);
  });

});