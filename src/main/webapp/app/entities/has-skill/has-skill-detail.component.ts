import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { HasSkill } from './has-skill.model';
import { HasSkillService } from './has-skill.service';

@Component({
    selector: 'jhi-has-skill-detail',
    templateUrl: './has-skill-detail.component.html'
})
export class HasSkillDetailComponent implements OnInit, OnDestroy {

    hasSkill: HasSkill;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private hasSkillService: HasSkillService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInHasSkills();
    }

    load(id) {
        this.hasSkillService.find(id).subscribe((hasSkill) => {
            this.hasSkill = hasSkill;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInHasSkills() {
        this.eventSubscriber = this.eventManager.subscribe(
            'hasSkillListModification',
            (response) => this.load(this.hasSkill.id)
        );
    }
}
