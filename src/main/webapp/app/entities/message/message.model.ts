import { Proposal } from '../proposal';
import { ProposalStatusCatalog } from '../proposal-status-catalog';
import { User } from '../../shared';
import { Attachment } from '../attachment';
export class Message {
    constructor(
        public id?: number,
        public messageTime?: any,
        public messageText?: string,
        public proposal?: Proposal,
        public proposalStatusCatalog?: ProposalStatusCatalog,
        public user?: User,
        public attachment?: Attachment,
    ) {
    }
}
