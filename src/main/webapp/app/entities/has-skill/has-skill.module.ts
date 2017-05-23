import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DiDomSharedModule } from '../../shared';
import { DiDomAdminModule } from '../../admin/admin.module';
import {
    HasSkillService,
    HasSkillPopupService,
    HasSkillComponent,
    HasSkillDetailComponent,
    HasSkillDialogComponent,
    HasSkillPopupComponent,
    HasSkillDeletePopupComponent,
    HasSkillDeleteDialogComponent,
    hasSkillRoute,
    hasSkillPopupRoute,
} from './';

const ENTITY_STATES = [
    ...hasSkillRoute,
    ...hasSkillPopupRoute,
];

@NgModule({
    imports: [
        DiDomSharedModule,
        DiDomAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        HasSkillComponent,
        HasSkillDetailComponent,
        HasSkillDialogComponent,
        HasSkillDeleteDialogComponent,
        HasSkillPopupComponent,
        HasSkillDeletePopupComponent,
    ],
    entryComponents: [
        HasSkillComponent,
        HasSkillDialogComponent,
        HasSkillPopupComponent,
        HasSkillDeleteDialogComponent,
        HasSkillDeletePopupComponent,
    ],
    providers: [
        HasSkillService,
        HasSkillPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DiDomHasSkillModule {}
