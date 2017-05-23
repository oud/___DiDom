import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { PaymentType } from './payment-type.model';
import { PaymentTypePopupService } from './payment-type-popup.service';
import { PaymentTypeService } from './payment-type.service';

@Component({
    selector: 'jhi-payment-type-dialog',
    templateUrl: './payment-type-dialog.component.html'
})
export class PaymentTypeDialogComponent implements OnInit {

    paymentType: PaymentType;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private paymentTypeService: PaymentTypeService,
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
        if (this.paymentType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.paymentTypeService.update(this.paymentType));
        } else {
            this.subscribeToSaveResponse(
                this.paymentTypeService.create(this.paymentType));
        }
    }

    private subscribeToSaveResponse(result: Observable<PaymentType>) {
        result.subscribe((res: PaymentType) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: PaymentType) {
        this.eventManager.broadcast({ name: 'paymentTypeListModification', content: 'OK'});
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
    selector: 'jhi-payment-type-popup',
    template: ''
})
export class PaymentTypePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private paymentTypePopupService: PaymentTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.paymentTypePopupService
                    .open(PaymentTypeDialogComponent, params['id']);
            } else {
                this.modalRef = this.paymentTypePopupService
                    .open(PaymentTypeDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
