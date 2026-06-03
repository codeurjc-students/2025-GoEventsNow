import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { Observable } from "rxjs";
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from "@angular/router";
import { Participant } from "../../model/participant";
import { ParticipantService } from "../../service/participant.service";
import { EventService } from "../../service/event.service";
import { UserService } from "../../service/user.service";
import { User } from "../../model/user";

@Component({
    standalone: true,
    selector: 'app-participant-detail',
    templateUrl: './participant-detail.component.html',
    imports: [CommonModule]
})

export class ParticipantDetailComponent implements OnInit {

    participant$: Observable<Participant> = new Observable<Participant>();
    events: any[] = [];
    currentUser: User | null = null;
    isFollowing: boolean = false;
    displayFollowerCount = 0;
    currentParticipantId: number | null = null;

    constructor(private  readonly activatedRoute: ActivatedRoute, private  readonly participantService: ParticipantService, private  readonly eventService:EventService,private  readonly cd: ChangeDetectorRef, private  readonly router: Router, private  readonly userService: UserService) { }

    ngOnInit(): void {
        const id = this.activatedRoute.snapshot.params['id'];
        this.currentParticipantId = Number(id);
        this.participant$ = this.participantService.findById(id);
        this.participant$.subscribe({
            next: (participant) => {
                this.displayFollowerCount = participant.numFollowers || 0;
            }
        });
        this.userService.getCurrentUser().subscribe({
            next: (user) => {
                this.currentUser = user;
                this.isFollowing = user.followedParticipants?.some(p => p.id === Number(id)) || false;
                this.cd.detectChanges();
            },
            error: () => {
                this.currentUser = null;
                this.isFollowing = false;
            }
        });
        this.participant$.subscribe({
            next: () => {
                this.eventService.getEventsByParticipantId(id).subscribe({
                    next: (events) => {
                        this.events = events;
                        this.cd.detectChanges();
                    },
                    error: (err) => console.error(err)
                });
            },
            error: () => this.router.navigate(['/error/404'])
        });
    }

    toggleFollow(): void {
        
        if (!this.currentUser?.id || !this.currentParticipantId) return;

        const userId = Number(this.currentUser.id);
        const participantId = Number(this.currentParticipantId);

        if (this.isFollowing) {
            this.userService.unfollowParticipant(userId, participantId).subscribe({
                next: () => {
                    this.isFollowing = false;
                    this.displayFollowerCount = Math.max(0, this.displayFollowerCount - 1);
                    this.cd.detectChanges();
    
                },
                error: (err) => console.error('Failed to unfollow participant', err)
            });
        } else {
            this.userService.followParticipant(userId, participantId).subscribe({
                next: () => {
                    this.isFollowing = true;
                    this.displayFollowerCount = this.displayFollowerCount + 1;
                    this.cd.detectChanges();
               
                },
                error: (err) => console.error('Failed to follow participant', err)
            });
        }
    }
}