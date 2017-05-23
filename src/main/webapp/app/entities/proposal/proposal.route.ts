import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ProposalComponent } from './proposal.component';
import { ProposalDetailComponent } from './proposal-detail.component';
import { ProposalPopupComponent } from './proposal-dialog.component';
import { ProposalDeletePopupComponent } from './proposal-delete-dialog.component';

import { Principal } from '../../shared';

export const proposalRoute: Routes = [
    {
        path: 'proposal',
        component: ProposalComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.proposal.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'proposal/:id',
        component: ProposalDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.proposal.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const proposalPopupRoute: Routes = [
    {
        path: 'proposal-new',
        component: ProposalPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.proposal.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'proposal/:id/edit',
        component: ProposalPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.proposal.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'proposal/:id/delete',
        component: ProposalDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.proposal.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
