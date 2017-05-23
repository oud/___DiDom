import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ProposalStatusCatalogComponent } from './proposal-status-catalog.component';
import { ProposalStatusCatalogDetailComponent } from './proposal-status-catalog-detail.component';
import { ProposalStatusCatalogPopupComponent } from './proposal-status-catalog-dialog.component';
import { ProposalStatusCatalogDeletePopupComponent } from './proposal-status-catalog-delete-dialog.component';

import { Principal } from '../../shared';

export const proposalStatusCatalogRoute: Routes = [
    {
        path: 'proposal-status-catalog',
        component: ProposalStatusCatalogComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.proposalStatusCatalog.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'proposal-status-catalog/:id',
        component: ProposalStatusCatalogDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.proposalStatusCatalog.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const proposalStatusCatalogPopupRoute: Routes = [
    {
        path: 'proposal-status-catalog-new',
        component: ProposalStatusCatalogPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.proposalStatusCatalog.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'proposal-status-catalog/:id/edit',
        component: ProposalStatusCatalogPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.proposalStatusCatalog.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'proposal-status-catalog/:id/delete',
        component: ProposalStatusCatalogDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.proposalStatusCatalog.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
