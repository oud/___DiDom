import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Proposal } from './proposal.model';
import { ProposalPopupService } from './proposal-popup.service';
import { ProposalService } from './proposal.service';
import { Job, JobService } from '../job';
import { User, UserService } from '../../shared';
import { PaymentType, PaymentTypeService } from '../payment-type';
import { ProposalStatusCatalog, ProposalStatusCatalogService } from '../proposal-status-catalog';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-proposal-dialog',
    templateUrl: './proposal-dialog.component.html'
})
export class ProposalDialogComponent implements OnInit {

    proposal: Proposal;
    authorities: any[];
    isSaving: boolean;

    jobs: Job[];

    users: User[];

    paymenttypes: PaymentType[];

    proposalstatuscatalogs: ProposalStatusCatalog[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private proposalService: ProposalService,
        private jobService: JobService,
        private userService: UserService,
        private paymentTypeService: PaymentTypeService,
        private proposalStatusCatalogService: ProposalStatusCatalogService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.jobService.query()
            .subscribe((res: ResponseWrapper) => { this.jobs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.paymentTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.paymenttypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.proposalStatusCatalogService.query()
            .subscribe((res: ResponseWrapper) => { this.proposalstatuscatalogs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.proposal.id !== undefined) {
            this.subscribeToSaveResponse(
                this.proposalService.update(this.proposal));
        } else {
            this.subscribeToSaveResponse(
                this.proposalService.create(this.proposal));
        }
    }

    private subscribeToSaveResponse(result: Observable<Proposal>) {
        result.subscribe((res: Proposal) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Proposal) {
        this.eventManager.broadcast({ name: 'proposalListModification', content: 'OK'});
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

    trackJobById(index: number, item: Job) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackPaymentTypeById(index: number, item: PaymentType) {
        return item.id;
    }

    trackProposalStatusCatalogById(index: number, item: ProposalStatusCatalog) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-proposal-popup',
    template: ''
})
export class ProposalPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private proposalPopupService: ProposalPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.proposalPopupService
                    .open(ProposalDialogComponent, params['id']);
            } else {
                this.modalRef = this.proposalPopupService
                    .open(ProposalDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
