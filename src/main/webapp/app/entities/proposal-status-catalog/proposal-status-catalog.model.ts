import { Proposal } from '../proposal';
import { Message } from '../message';
export class ProposalStatusCatalog {
    constructor(
        public id?: number,
        public statusName?: string,
        public proposal?: Proposal,
        public message?: Message,
    ) {
    }
}
