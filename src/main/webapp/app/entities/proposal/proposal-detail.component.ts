import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Proposal } from './proposal.model';
import { ProposalService } from './proposal.service';

@Component({
    selector: 'jhi-proposal-detail',
    templateUrl: './proposal-detail.component.html'
})
export class ProposalDetailComponent implements OnInit, OnDestroy {

    proposal: Proposal;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private proposalService: ProposalService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProposals();
    }

    load(id) {
        this.proposalService.find(id).subscribe((proposal) => {
            this.proposal = proposal;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProposals() {
        this.eventSubscriber = this.eventManager.subscribe(
            'proposalListModification',
            (response) => this.load(this.proposal.id)
        );
    }
}
