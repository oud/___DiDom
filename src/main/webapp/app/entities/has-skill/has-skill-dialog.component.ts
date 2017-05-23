import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { HasSkill } from './has-skill.model';
import { HasSkillPopupService } from './has-skill-popup.service';
import { HasSkillService } from './has-skill.service';
import { User, UserService } from '../../shared';
import { Skill, SkillService } from '../skill';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-has-skill-dialog',
    templateUrl: './has-skill-dialog.component.html'
})
export class HasSkillDialogComponent implements OnInit {

    hasSkill: HasSkill;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    skills: Skill[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private hasSkillService: HasSkillService,
        private userService: UserService,
        private skillService: SkillService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.skillService.query()
            .subscribe((res: ResponseWrapper) => { this.skills = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.hasSkill.id !== undefined) {
            this.subscribeToSaveResponse(
                this.hasSkillService.update(this.hasSkill));
        } else {
            this.subscribeToSaveResponse(
                this.hasSkillService.create(this.hasSkill));
        }
    }

    private subscribeToSaveResponse(result: Observable<HasSkill>) {
        result.subscribe((res: HasSkill) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: HasSkill) {
        this.eventManager.broadcast({ name: 'hasSkillListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackSkillById(index: number, item: Skill) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-has-skill-popup',
    template: ''
})
export class HasSkillPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private hasSkillPopupService: HasSkillPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.hasSkillPopupService
                    .open(HasSkillDialogComponent, params['id']);
            } else {
                this.modalRef = this.hasSkillPopupService
                    .open(HasSkillDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
