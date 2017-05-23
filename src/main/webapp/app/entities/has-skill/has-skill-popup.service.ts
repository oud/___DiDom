import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HasSkill } from './has-skill.model';
import { HasSkillService } from './has-skill.service';
@Injectable()
export class HasSkillPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private hasSkillService: HasSkillService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.hasSkillService.find(id).subscribe((hasSkill) => {
                this.hasSkillModalRef(component, hasSkill);
            });
        } else {
            return this.hasSkillModalRef(component, new HasSkill());
        }
    }

    hasSkillModalRef(component: Component, hasSkill: HasSkill): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.hasSkill = hasSkill;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
