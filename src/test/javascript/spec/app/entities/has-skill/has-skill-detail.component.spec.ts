import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { DiDomTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { HasSkillDetailComponent } from '../../../../../../main/webapp/app/entities/has-skill/has-skill-detail.component';
import { HasSkillService } from '../../../../../../main/webapp/app/entities/has-skill/has-skill.service';
import { HasSkill } from '../../../../../../main/webapp/app/entities/has-skill/has-skill.model';

describe('Component Tests', () => {

    describe('HasSkill Management Detail Component', () => {
        let comp: HasSkillDetailComponent;
        let fixture: ComponentFixture<HasSkillDetailComponent>;
        let service: HasSkillService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DiDomTestModule],
                declarations: [HasSkillDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    HasSkillService,
                    EventManager
                ]
            }).overrideComponent(HasSkillDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(HasSkillDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HasSkillService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new HasSkill(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.hasSkill).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
