import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Attachment } from './attachment.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AttachmentService {

    private resourceUrl = 'api/attachments';
    private resourceSearchUrl = 'api/_search/attachments';

    constructor(private http: Http) { }

    create(attachment: Attachment): Observable<Attachment> {
        const copy = this.convert(attachment);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(attachment: Attachment): Observable<Attachment> {
        const copy = this.convert(attachment);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Attachment> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse);
    }

    private convert(attachment: Attachment): Attachment {
        const copy: Attachment = Object.assign({}, attachment);
        return copy;
    }
}
