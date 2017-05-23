import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { PaymentTypeComponent } from './payment-type.component';
import { PaymentTypeDetailComponent } from './payment-type-detail.component';
import { PaymentTypePopupComponent } from './payment-type-dialog.component';
import { PaymentTypeDeletePopupComponent } from './payment-type-delete-dialog.component';

import { Principal } from '../../shared';

export const paymentTypeRoute: Routes = [
    {
        path: 'payment-type',
        component: PaymentTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.paymentType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'payment-type/:id',
        component: PaymentTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.paymentType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const paymentTypePopupRoute: Routes = [
    {
        path: 'payment-type-new',
        component: PaymentTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.paymentType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'payment-type/:id/edit',
        component: PaymentTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.paymentType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'payment-type/:id/delete',
        component: PaymentTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.paymentType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
