import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { HasSkill } from './has-skill.model';
import { HasSkillService } from './has-skill.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-has-skill',
    templateUrl: './has-skill.component.html'
})
export class HasSkillComponent implements OnInit, OnDestroy {
hasSkills: HasSkill[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private hasSkillService: HasSkillService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.hasSkillService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.hasSkills = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.hasSkillService.query().subscribe(
            (res: ResponseWrapper) => {
                this.hasSkills = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInHasSkills();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: HasSkill) {
        return item.id;
    }
    registerChangeInHasSkills() {
        this.eventSubscriber = this.eventManager.subscribe('hasSkillListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
