import { Job } from '../job';
import { User } from '../../shared';
import { PaymentType } from '../payment-type';
import { ProposalStatusCatalog } from '../proposal-status-catalog';
import { Message } from '../message';
export class Proposal {
    constructor(
        public id?: number,
        public proposalTime?: any,
        public paymentAmount?: number,
        public clientGrade?: number,
        public clientComment?: string,
        public freelanceGrade?: number,
        public freelanceComment?: string,
        public job?: Job,
        public user?: User,
        public paymentType?: PaymentType,
        public currentProposalStatus?: ProposalStatusCatalog,
        public message?: Message,
    ) {
    }
}
