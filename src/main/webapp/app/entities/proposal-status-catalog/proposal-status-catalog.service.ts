import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { ProposalStatusCatalog } from './proposal-status-catalog.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ProposalStatusCatalogService {

    private resourceUrl = 'api/proposal-status-catalogs';
    private resourceSearchUrl = 'api/_search/proposal-status-catalogs';

    constructor(private http: Http) { }

    create(proposalStatusCatalog: ProposalStatusCatalog): Observable<ProposalStatusCatalog> {
        const copy = this.convert(proposalStatusCatalog);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(proposalStatusCatalog: ProposalStatusCatalog): Observable<ProposalStatusCatalog> {
        const copy = this.convert(proposalStatusCatalog);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<ProposalStatusCatalog> {
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

    private convert(proposalStatusCatalog: ProposalStatusCatalog): ProposalStatusCatalog {
        const copy: ProposalStatusCatalog = Object.assign({}, proposalStatusCatalog);
        return copy;
    }
}
