import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { HasSkill } from './has-skill.model';
import { HasSkillPopupService } from './has-skill-popup.service';
import { HasSkillService } from './has-skill.service';

@Component({
    selector: 'jhi-has-skill-delete-dialog',
    templateUrl: './has-skill-delete-dialog.component.html'
})
export class HasSkillDeleteDialogComponent {

    hasSkill: HasSkill;

    constructor(
        private hasSkillService: HasSkillService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.hasSkillService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'hasSkillListModification',
                content: 'Deleted an hasSkill'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-has-skill-delete-popup',
    template: ''
})
export class HasSkillDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private hasSkillPopupService: HasSkillPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.hasSkillPopupService
                .open(HasSkillDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
