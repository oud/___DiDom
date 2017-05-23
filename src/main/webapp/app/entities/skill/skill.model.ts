import { Job } from '../job';
export class Skill {
    constructor(
        public id?: number,
        public skillName?: string,
        public job?: Job,
    ) {
    }
}
