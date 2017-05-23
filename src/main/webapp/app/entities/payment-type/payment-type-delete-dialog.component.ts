import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { PaymentType } from './payment-type.model';
import { PaymentTypePopupService } from './payment-type-popup.service';
import { PaymentTypeService } from './payment-type.service';

@Component({
    selector: 'jhi-payment-type-delete-dialog',
    templateUrl: './payment-type-delete-dialog.component.html'
})
export class PaymentTypeDeleteDialogComponent {

    paymentType: PaymentType;

    constructor(
        private paymentTypeService: PaymentTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.paymentTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'paymentTypeListModification',
                content: 'Deleted an paymentType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-payment-type-delete-popup',
    template: ''
})
export class PaymentTypeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private paymentTypePopupService: PaymentTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.paymentTypePopupService
                .open(PaymentTypeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
