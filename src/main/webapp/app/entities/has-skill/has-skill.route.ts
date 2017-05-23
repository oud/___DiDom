import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { HasSkillComponent } from './has-skill.component';
import { HasSkillDetailComponent } from './has-skill-detail.component';
import { HasSkillPopupComponent } from './has-skill-dialog.component';
import { HasSkillDeletePopupComponent } from './has-skill-delete-dialog.component';

import { Principal } from '../../shared';

export const hasSkillRoute: Routes = [
    {
        path: 'has-skill',
        component: HasSkillComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.hasSkill.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'has-skill/:id',
        component: HasSkillDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.hasSkill.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const hasSkillPopupRoute: Routes = [
    {
        path: 'has-skill-new',
        component: HasSkillPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.hasSkill.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'has-skill/:id/edit',
        component: HasSkillPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.hasSkill.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'has-skill/:id/delete',
        component: HasSkillDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'diDomApp.hasSkill.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
