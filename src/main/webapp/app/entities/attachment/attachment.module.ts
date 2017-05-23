import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DiDomSharedModule } from '../../shared';
import {
    AttachmentService,
    AttachmentPopupService,
    AttachmentComponent,
    AttachmentDetailComponent,
    AttachmentDialogComponent,
    AttachmentPopupComponent,
    AttachmentDeletePopupComponent,
    AttachmentDeleteDialogComponent,
    attachmentRoute,
    attachmentPopupRoute,
} from './';

const ENTITY_STATES = [
    ...attachmentRoute,
    ...attachmentPopupRoute,
];

@NgModule({
    imports: [
        DiDomSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AttachmentComponent,
        AttachmentDetailComponent,
        AttachmentDialogComponent,
        AttachmentDeleteDialogComponent,
        AttachmentPopupComponent,
        AttachmentDeletePopupComponent,
    ],
    entryComponents: [
        AttachmentComponent,
        AttachmentDialogComponent,
        AttachmentPopupComponent,
        AttachmentDeleteDialogComponent,
        AttachmentDeletePopupComponent,
    ],
    providers: [
        AttachmentService,
        AttachmentPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DiDomAttachmentModule {}
