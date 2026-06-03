import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface GraphicResponse {
  labels: string[];
  data: number[];
  backgroundColor: string[];
}

const BASE_URL = '/api/v1/graphics';

@Injectable({ providedIn: 'root' })
export class GraphicService {

  constructor(private  readonly httpClient: HttpClient) {}

  getTicketsSoldByEvent(): Observable<GraphicResponse> {
    return this.httpClient.get<GraphicResponse>(`${BASE_URL}/bargraph`);
  }

  getTicketsSoldByCategory(): Observable<GraphicResponse> {
    return this.httpClient.get<GraphicResponse>(`${BASE_URL}/piechart`);
  }
}