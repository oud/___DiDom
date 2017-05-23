import { User } from '../../shared';
import { Skill } from '../skill';
export class HasSkill {
    constructor(
        public id?: number,
        public user?: User,
        public skill?: Skill,
    ) {
    }
}
