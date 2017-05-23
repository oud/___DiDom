import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { DiDomTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PaymentTypeDetailComponent } from '../../../../../../main/webapp/app/entities/payment-type/payment-type-detail.component';
import { PaymentTypeService } from '../../../../../../main/webapp/app/entities/payment-type/payment-type.service';
import { PaymentType } from '../../../../../../main/webapp/app/entities/payment-type/payment-type.model';

describe('Component Tests', () => {

    describe('PaymentType Management Detail Component', () => {
        let comp: PaymentTypeDetailComponent;
        let fixture: ComponentFixture<PaymentTypeDetailComponent>;
        let service: PaymentTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DiDomTestModule],
                declarations: [PaymentTypeDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PaymentTypeService,
                    EventManager
                ]
            }).overrideComponent(PaymentTypeDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PaymentTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PaymentTypeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PaymentType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.paymentType).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
