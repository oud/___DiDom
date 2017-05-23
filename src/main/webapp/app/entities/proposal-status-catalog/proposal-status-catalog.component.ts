import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { ProposalStatusCatalog } from './proposal-status-catalog.model';
import { ProposalStatusCatalogService } from './proposal-status-catalog.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-proposal-status-catalog',
    templateUrl: './proposal-status-catalog.component.html'
})
export class ProposalStatusCatalogComponent implements OnInit, OnDestroy {
proposalStatusCatalogs: ProposalStatusCatalog[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private proposalStatusCatalogService: ProposalStatusCatalogService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.proposalStatusCatalogService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.proposalStatusCatalogs = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.proposalStatusCatalogService.query().subscribe(
            (res: ResponseWrapper) => {
                this.proposalStatusCatalogs = res.json;
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
        this.registerChangeInProposalStatusCatalogs();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ProposalStatusCatalog) {
        return item.id;
    }
    registerChangeInProposalStatusCatalogs() {
        this.eventSubscriber = this.eventManager.subscribe('proposalStatusCatalogListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
