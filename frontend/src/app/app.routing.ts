import { Routes } from '@angular/router';

import { EventListComponent } from './components/events/event-list.component';
import { ErrorComponent } from './components/error/error.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AllEventsComponent } from './components/events/all-events.component';
import { EventDetailComponent } from './components/events/event-detail.component';
import { ManageEventsComponent } from './components/events/manage-events.component';
import { AddEventComponent } from './components/events/add-event.component';
import { ParticipantsListComponent } from './components/participants/participants-list.component';
import { ParticipantDetailComponent } from './components/participants/participant-detail.component';
import { ManageParticipantsComponent } from './components/participants/manage-participants.component';
import { AddParticipantsComponent } from './components/participants/add-participants.component';
import { UserPageComponent } from './components/user/user-page.component';
import { TicketSelectionComponent } from './components/tickets/ticket-selection.component';
import { AdminGuard } from './admin.guard';
import { authGuard } from './auth.guard';
import { HelpComponent } from './components/help/help.component';
import { GraphicsDashboardComponent } from './components/graphics/graphics-dashboard.component';

export const appRoutes: Routes = [

    { path: '', component: EventListComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'events', component: AllEventsComponent },
    { path: 'event/:id', component: EventDetailComponent },
    { path: 'manage-events', component: ManageEventsComponent, canActivate: [AdminGuard] },
    { path: 'create-event', component: AddEventComponent, canActivate: [AdminGuard] },
    { path: 'edit-event/:id', component: AddEventComponent, canActivate: [AdminGuard] },
    { path: 'participants', component: ParticipantsListComponent},
    { path: 'participant/:id', component: ParticipantDetailComponent},
    { path: 'manage-participants', component: ManageParticipantsComponent, canActivate: [AdminGuard] },
    { path: 'create-participant', component: AddParticipantsComponent, canActivate: [AdminGuard] },
    { path: 'edit-participant/:id', component: AddParticipantsComponent, canActivate: [AdminGuard] },
    { path: 'user/:id', component: UserPageComponent, canActivate: [authGuard] },
    { path: 'event/:id/tickets', component: TicketSelectionComponent, canActivate: [authGuard]},
    { path: 'help', component: HelpComponent },
    { path: 'graphics', component: GraphicsDashboardComponent, canActivate: [AdminGuard] },
    { path: 'error/:type', component: ErrorComponent },
    { path: '**', redirectTo: 'error/404' }

];

