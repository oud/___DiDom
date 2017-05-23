import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Skill } from './skill.model';
import { SkillService } from './skill.service';

@Component({
    selector: 'jhi-skill-detail',
    templateUrl: './skill-detail.component.html'
})
export class SkillDetailComponent implements OnInit, OnDestroy {

    skill: Skill;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private skillService: SkillService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSkills();
    }

    load(id) {
        this.skillService.find(id).subscribe((skill) => {
            this.skill = skill;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSkills() {
        this.eventSubscriber = this.eventManager.subscribe(
            'skillListModification',
            (response) => this.load(this.skill.id)
        );
    }
}
