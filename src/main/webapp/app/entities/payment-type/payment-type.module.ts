import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DiDomSharedModule } from '../../shared';
import {
    PaymentTypeService,
    PaymentTypePopupService,
    PaymentTypeComponent,
    PaymentTypeDetailComponent,
    PaymentTypeDialogComponent,
    PaymentTypePopupComponent,
    PaymentTypeDeletePopupComponent,
    PaymentTypeDeleteDialogComponent,
    paymentTypeRoute,
    paymentTypePopupRoute,
} from './';

const ENTITY_STATES = [
    ...paymentTypeRoute,
    ...paymentTypePopupRoute,
];

@NgModule({
    imports: [
        DiDomSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PaymentTypeComponent,
        PaymentTypeDetailComponent,
        PaymentTypeDialogComponent,
        PaymentTypeDeleteDialogComponent,
        PaymentTypePopupComponent,
        PaymentTypeDeletePopupComponent,
    ],
    entryComponents: [
        PaymentTypeComponent,
        PaymentTypeDialogComponent,
        PaymentTypePopupComponent,
        PaymentTypeDeleteDialogComponent,
        PaymentTypeDeletePopupComponent,
    ],
    providers: [
        PaymentTypeService,
        PaymentTypePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DiDomPaymentTypeModule {}
