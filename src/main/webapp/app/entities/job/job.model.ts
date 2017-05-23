
const enum Complexity {
    'EASY',
    'INTERMEDIATE',
    'HARD'

};
import { Skill } from '../skill';
import { PaymentType } from '../payment-type';
import { Duration } from '../duration';
import { User } from '../../shared';
import { Proposal } from '../proposal';
export class Job {
    constructor(
        public id?: number,
        public title?: string,
        public description?: string,
        public paymentAmont?: number,
        public complexity?: Complexity,
        public mainSkill?: Skill,
        public paymentType?: PaymentType,
        public duration?: Duration,
        public user?: User,
        public proposal?: Proposal,
    ) {
    }
}
