import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { DurationComponent } from './duration.component';
import { DurationDetailComponent } from './duration-detail.component';
import { DurationPopupComponent } from './duration-dialog.component';
import { DurationDeletePopupComponent } from './duration-delete-dialog.component';

import { Principal } from '../../shared';

export const durationRoute: Routes = [
    {
        path: 'duration',
        component: DurationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.duration.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'duration/:id',
        component: DurationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.duration.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const durationPopupRoute: Routes = [
    {
        path: 'duration-new',
        component: DurationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.duration.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'duration/:id/edit',
        component: DurationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.duration.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'duration/:id/delete',
        component: DurationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.duration.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
