import { Job } from '../job';
import { Proposal } from '../proposal';
export class PaymentType {
    constructor(
        public id?: number,
        public typeName?: string,
        public job?: Job,
        public proposal?: Proposal,
    ) {
    }
}
