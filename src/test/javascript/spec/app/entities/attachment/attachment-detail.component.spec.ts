import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { DiDomTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AttachmentDetailComponent } from '../../../../../../main/webapp/app/entities/attachment/attachment-detail.component';
import { AttachmentService } from '../../../../../../main/webapp/app/entities/attachment/attachment.service';
import { Attachment } from '../../../../../../main/webapp/app/entities/attachment/attachment.model';

describe('Component Tests', () => {

    describe('Attachment Management Detail Component', () => {
        let comp: AttachmentDetailComponent;
        let fixture: ComponentFixture<AttachmentDetailComponent>;
        let service: AttachmentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DiDomTestModule],
                declarations: [AttachmentDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AttachmentService,
                    EventManager
                ]
            }).overrideComponent(AttachmentDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AttachmentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttachmentService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Attachment(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.attachment).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
