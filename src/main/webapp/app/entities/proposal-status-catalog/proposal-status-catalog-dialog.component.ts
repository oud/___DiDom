import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { ProposalStatusCatalog } from './proposal-status-catalog.model';
import { ProposalStatusCatalogPopupService } from './proposal-status-catalog-popup.service';
import { ProposalStatusCatalogService } from './proposal-status-catalog.service';

@Component({
    selector: 'jhi-proposal-status-catalog-dialog',
    templateUrl: './proposal-status-catalog-dialog.component.html'
})
export class ProposalStatusCatalogDialogComponent implements OnInit {

    proposalStatusCatalog: ProposalStatusCatalog;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private proposalStatusCatalogService: ProposalStatusCatalogService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.proposalStatusCatalog.id !== undefined) {
            this.subscribeToSaveResponse(
                this.proposalStatusCatalogService.update(this.proposalStatusCatalog));
        } else {
            this.subscribeToSaveResponse(
                this.proposalStatusCatalogService.create(this.proposalStatusCatalog));
        }
    }

    private subscribeToSaveResponse(result: Observable<ProposalStatusCatalog>) {
        result.subscribe((res: ProposalStatusCatalog) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ProposalStatusCatalog) {
        this.eventManager.broadcast({ name: 'proposalStatusCatalogListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-proposal-status-catalog-popup',
    template: ''
})
export class ProposalStatusCatalogPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private proposalStatusCatalogPopupService: ProposalStatusCatalogPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.proposalStatusCatalogPopupService
                    .open(ProposalStatusCatalogDialogComponent, params['id']);
            } else {
                this.modalRef = this.proposalStatusCatalogPopupService
                    .open(ProposalStatusCatalogDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
