import { Routes } from '@angular/router';

import { EventListComponent } from './components/events/event-list.component';
import { ErrorComponent } from './components/error/error.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AllEventsComponent } from './components/events/all-events.component';
import { EventDetailComponent } from './components/events/event-detail.component';

export const appRoutes: Routes = [

    { path: '', component: EventListComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'events', component: AllEventsComponent },
    { path: 'event/:id', component: EventDetailComponent },
    { path: 'error/:type', component: ErrorComponent },
    { path: '**', redirectTo: 'error/404' }

];

