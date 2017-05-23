import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { DiDomTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProposalStatusCatalogDetailComponent } from '../../../../../../main/webapp/app/entities/proposal-status-catalog/proposal-status-catalog-detail.component';
import { ProposalStatusCatalogService } from '../../../../../../main/webapp/app/entities/proposal-status-catalog/proposal-status-catalog.service';
import { ProposalStatusCatalog } from '../../../../../../main/webapp/app/entities/proposal-status-catalog/proposal-status-catalog.model';

describe('Component Tests', () => {

    describe('ProposalStatusCatalog Management Detail Component', () => {
        let comp: ProposalStatusCatalogDetailComponent;
        let fixture: ComponentFixture<ProposalStatusCatalogDetailComponent>;
        let service: ProposalStatusCatalogService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DiDomTestModule],
                declarations: [ProposalStatusCatalogDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProposalStatusCatalogService,
                    EventManager
                ]
            }).overrideComponent(ProposalStatusCatalogDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProposalStatusCatalogDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProposalStatusCatalogService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProposalStatusCatalog(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.proposalStatusCatalog).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
