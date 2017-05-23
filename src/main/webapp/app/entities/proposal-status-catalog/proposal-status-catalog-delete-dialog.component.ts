import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { ProposalStatusCatalog } from './proposal-status-catalog.model';
import { ProposalStatusCatalogPopupService } from './proposal-status-catalog-popup.service';
import { ProposalStatusCatalogService } from './proposal-status-catalog.service';

@Component({
    selector: 'jhi-proposal-status-catalog-delete-dialog',
    templateUrl: './proposal-status-catalog-delete-dialog.component.html'
})
export class ProposalStatusCatalogDeleteDialogComponent {

    proposalStatusCatalog: ProposalStatusCatalog;

    constructor(
        private proposalStatusCatalogService: ProposalStatusCatalogService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.proposalStatusCatalogService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'proposalStatusCatalogListModification',
                content: 'Deleted an proposalStatusCatalog'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-proposal-status-catalog-delete-popup',
    template: ''
})
export class ProposalStatusCatalogDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private proposalStatusCatalogPopupService: ProposalStatusCatalogPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.proposalStatusCatalogPopupService
                .open(ProposalStatusCatalogDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
