import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { AttachmentComponent } from './attachment.component';
import { AttachmentDetailComponent } from './attachment-detail.component';
import { AttachmentPopupComponent } from './attachment-dialog.component';
import { AttachmentDeletePopupComponent } from './attachment-delete-dialog.component';

import { Principal } from '../../shared';

export const attachmentRoute: Routes = [
    {
        path: 'attachment',
        component: AttachmentComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.attachment.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'attachment/:id',
        component: AttachmentDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.attachment.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const attachmentPopupRoute: Routes = [
    {
        path: 'attachment-new',
        component: AttachmentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.attachment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'attachment/:id/edit',
        component: AttachmentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.attachment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'attachment/:id/delete',
        component: AttachmentDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.attachment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
