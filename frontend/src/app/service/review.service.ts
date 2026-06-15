import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { map, catchError } from 'rxjs/operators';
import { Review } from "../model/review";

const BASE_URL = '/api/v1/reviews/';

@Injectable({ providedIn: 'root' })
export class ReviewService {

    constructor(private readonly httpClient: HttpClient) { }

    public getAllReviewsForEvent(eventId: number): Observable<Review[]> {
        const id = Number(eventId);
        return this.httpClient.get<any>(BASE_URL + 'event/' + id).pipe(
            map(response => response?.content || []),
            catchError(() => of([]))
        );
    }

    public getAllReviewsForUsername(username: string): Observable<Review[]> {
        const path = `user/${encodeURIComponent(username)}`;
        return this.httpClient.get<any>(BASE_URL + path).pipe(
            map(response => response?.content || []),
            catchError(() => of([]))
        );
    }

    public deleteById(reviewId: number | string): Observable<Review> {
        const id = Number(reviewId);
        return this.httpClient.delete<Review>(BASE_URL + id);
    }

    public createOrReplaceReview(review: Review): Observable<Review> {
        if (review.id) {
            const id = Number(review.id);
            return this.httpClient.put<Review>(BASE_URL + id, review);
        } else {
            const eventAssociatedId = Number(review.eventAssociatedId);
            return this.httpClient.post<Review>(BASE_URL + 'event/' + eventAssociatedId, review);
        }
    }

}
