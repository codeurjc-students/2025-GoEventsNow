import { Routes } from '@angular/router';

import { EventListComponent } from './components/events/event-list.component';
import { ErrorComponent } from './components/error/error.component';
import { LoginComponent } from './components/login/login.component';

export const appRoutes: Routes = [

    { path: '', component: EventListComponent },
    { path: 'login', component: LoginComponent },
    { path: 'error/:type', component: ErrorComponent },
    { path: '**', redirectTo: 'error/404' }

];

