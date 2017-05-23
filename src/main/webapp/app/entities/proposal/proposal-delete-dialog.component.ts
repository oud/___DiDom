import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Proposal } from './proposal.model';
import { ProposalPopupService } from './proposal-popup.service';
import { ProposalService } from './proposal.service';

@Component({
    selector: 'jhi-proposal-delete-dialog',
    templateUrl: './proposal-delete-dialog.component.html'
})
export class ProposalDeleteDialogComponent {

    proposal: Proposal;

    constructor(
        private proposalService: ProposalService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.proposalService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'proposalListModification',
                content: 'Deleted an proposal'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-proposal-delete-popup',
    template: ''
})
export class ProposalDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private proposalPopupService: ProposalPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.proposalPopupService
                .open(ProposalDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
