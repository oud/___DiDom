import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { PaymentType } from './payment-type.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PaymentTypeService {

    private resourceUrl = 'api/payment-types';
    private resourceSearchUrl = 'api/_search/payment-types';

    constructor(private http: Http) { }

    create(paymentType: PaymentType): Observable<PaymentType> {
        const copy = this.convert(paymentType);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(paymentType: PaymentType): Observable<PaymentType> {
        const copy = this.convert(paymentType);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<PaymentType> {
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

    private convert(paymentType: PaymentType): PaymentType {
        const copy: PaymentType = Object.assign({}, paymentType);
        return copy;
    }
}
