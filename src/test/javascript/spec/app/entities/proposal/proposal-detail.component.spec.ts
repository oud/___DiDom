import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { DiDomTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProposalDetailComponent } from '../../../../../../main/webapp/app/entities/proposal/proposal-detail.component';
import { ProposalService } from '../../../../../../main/webapp/app/entities/proposal/proposal.service';
import { Proposal } from '../../../../../../main/webapp/app/entities/proposal/proposal.model';

describe('Component Tests', () => {

    describe('Proposal Management Detail Component', () => {
        let comp: ProposalDetailComponent;
        let fixture: ComponentFixture<ProposalDetailComponent>;
        let service: ProposalService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DiDomTestModule],
                declarations: [ProposalDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProposalService,
                    EventManager
                ]
            }).overrideComponent(ProposalDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProposalDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProposalService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Proposal(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.proposal).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
