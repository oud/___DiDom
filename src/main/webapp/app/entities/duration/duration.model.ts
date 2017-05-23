import { Job } from '../job';
export class Duration {
    constructor(
        public id?: number,
        public duration?: string,
        public job?: Job,
    ) {
    }
}
