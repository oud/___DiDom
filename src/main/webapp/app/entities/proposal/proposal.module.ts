import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DiDomSharedModule } from '../../shared';
import { DiDomAdminModule } from '../../admin/admin.module';
import {
    ProposalService,
    ProposalPopupService,
    ProposalComponent,
    ProposalDetailComponent,
    ProposalDialogComponent,
    ProposalPopupComponent,
    ProposalDeletePopupComponent,
    ProposalDeleteDialogComponent,
    proposalRoute,
    proposalPopupRoute,
} from './';

const ENTITY_STATES = [
    ...proposalRoute,
    ...proposalPopupRoute,
];

@NgModule({
    imports: [
        DiDomSharedModule,
        DiDomAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProposalComponent,
        ProposalDetailComponent,
        ProposalDialogComponent,
        ProposalDeleteDialogComponent,
        ProposalPopupComponent,
        ProposalDeletePopupComponent,
    ],
    entryComponents: [
        ProposalComponent,
        ProposalDialogComponent,
        ProposalPopupComponent,
        ProposalDeleteDialogComponent,
        ProposalDeletePopupComponent,
    ],
    providers: [
        ProposalService,
        ProposalPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DiDomProposalModule {}
