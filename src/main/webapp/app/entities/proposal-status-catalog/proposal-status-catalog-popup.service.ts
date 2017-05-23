import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ProposalStatusCatalog } from './proposal-status-catalog.model';
import { ProposalStatusCatalogService } from './proposal-status-catalog.service';
@Injectable()
export class ProposalStatusCatalogPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private proposalStatusCatalogService: ProposalStatusCatalogService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.proposalStatusCatalogService.find(id).subscribe((proposalStatusCatalog) => {
                this.proposalStatusCatalogModalRef(component, proposalStatusCatalog);
            });
        } else {
            return this.proposalStatusCatalogModalRef(component, new ProposalStatusCatalog());
        }
    }

    proposalStatusCatalogModalRef(component: Component, proposalStatusCatalog: ProposalStatusCatalog): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.proposalStatusCatalog = proposalStatusCatalog;
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
