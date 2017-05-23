import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Attachment } from './attachment.model';
import { AttachmentPopupService } from './attachment-popup.service';
import { AttachmentService } from './attachment.service';
import { Message, MessageService } from '../message';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-attachment-dialog',
    templateUrl: './attachment-dialog.component.html'
})
export class AttachmentDialogComponent implements OnInit {

    attachment: Attachment;
    authorities: any[];
    isSaving: boolean;

    messages: Message[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private attachmentService: AttachmentService,
        private messageService: MessageService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.messageService.query()
            .subscribe((res: ResponseWrapper) => { this.messages = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.attachment.id !== undefined) {
            this.subscribeToSaveResponse(
                this.attachmentService.update(this.attachment));
        } else {
            this.subscribeToSaveResponse(
                this.attachmentService.create(this.attachment));
        }
    }

    private subscribeToSaveResponse(result: Observable<Attachment>) {
        result.subscribe((res: Attachment) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Attachment) {
        this.eventManager.broadcast({ name: 'attachmentListModification', content: 'OK'});
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

    trackMessageById(index: number, item: Message) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-attachment-popup',
    template: ''
})
export class AttachmentPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private attachmentPopupService: AttachmentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.attachmentPopupService
                    .open(AttachmentDialogComponent, params['id']);
            } else {
                this.modalRef = this.attachmentPopupService
                    .open(AttachmentDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
