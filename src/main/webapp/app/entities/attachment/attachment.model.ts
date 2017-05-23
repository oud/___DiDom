import { Message } from '../message';
export class Attachment {
    constructor(
        public id?: number,
        public attachmentLink?: string,
        public message?: Message,
    ) {
    }
}
