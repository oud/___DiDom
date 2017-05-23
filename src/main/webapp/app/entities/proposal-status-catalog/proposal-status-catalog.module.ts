import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DiDomSharedModule } from '../../shared';
import {
    ProposalStatusCatalogService,
    ProposalStatusCatalogPopupService,
    ProposalStatusCatalogComponent,
    ProposalStatusCatalogDetailComponent,
    ProposalStatusCatalogDialogComponent,
    ProposalStatusCatalogPopupComponent,
    ProposalStatusCatalogDeletePopupComponent,
    ProposalStatusCatalogDeleteDialogComponent,
    proposalStatusCatalogRoute,
    proposalStatusCatalogPopupRoute,
} from './';

const ENTITY_STATES = [
    ...proposalStatusCatalogRoute,
    ...proposalStatusCatalogPopupRoute,
];

@NgModule({
    imports: [
        DiDomSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProposalStatusCatalogComponent,
        ProposalStatusCatalogDetailComponent,
        ProposalStatusCatalogDialogComponent,
        ProposalStatusCatalogDeleteDialogComponent,
        ProposalStatusCatalogPopupComponent,
        ProposalStatusCatalogDeletePopupComponent,
    ],
    entryComponents: [
        ProposalStatusCatalogComponent,
        ProposalStatusCatalogDialogComponent,
        ProposalStatusCatalogPopupComponent,
        ProposalStatusCatalogDeleteDialogComponent,
        ProposalStatusCatalogDeletePopupComponent,
    ],
    providers: [
        ProposalStatusCatalogService,
        ProposalStatusCatalogPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DiDomProposalStatusCatalogModule {}
