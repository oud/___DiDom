import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Duration } from './duration.model';
import { DurationPopupService } from './duration-popup.service';
import { DurationService } from './duration.service';

@Component({
    selector: 'jhi-duration-dialog',
    templateUrl: './duration-dialog.component.html'
})
export class DurationDialogComponent implements OnInit {

    duration: Duration;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private durationService: DurationService,
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
        if (this.duration.id !== undefined) {
            this.subscribeToSaveResponse(
                this.durationService.update(this.duration));
        } else {
            this.subscribeToSaveResponse(
                this.durationService.create(this.duration));
        }
    }

    private subscribeToSaveResponse(result: Observable<Duration>) {
        result.subscribe((res: Duration) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Duration) {
        this.eventManager.broadcast({ name: 'durationListModification', content: 'OK'});
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
    selector: 'jhi-duration-popup',
    template: ''
})
export class DurationPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private durationPopupService: DurationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.durationPopupService
                    .open(DurationDialogComponent, params['id']);
            } else {
                this.modalRef = this.durationPopupService
                    .open(DurationDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
