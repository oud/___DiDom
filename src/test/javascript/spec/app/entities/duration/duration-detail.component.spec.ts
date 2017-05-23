import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { DiDomTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { DurationDetailComponent } from '../../../../../../main/webapp/app/entities/duration/duration-detail.component';
import { DurationService } from '../../../../../../main/webapp/app/entities/duration/duration.service';
import { Duration } from '../../../../../../main/webapp/app/entities/duration/duration.model';

describe('Component Tests', () => {

    describe('Duration Management Detail Component', () => {
        let comp: DurationDetailComponent;
        let fixture: ComponentFixture<DurationDetailComponent>;
        let service: DurationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DiDomTestModule],
                declarations: [DurationDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    DurationService,
                    EventManager
                ]
            }).overrideComponent(DurationDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DurationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DurationService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Duration(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.duration).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
