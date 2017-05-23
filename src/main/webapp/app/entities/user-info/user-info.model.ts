
const enum TypeUser {
    'HIRE',
    'SEEKER'

};
import { User } from '../../shared';
import { Location } from '../location';
export class UserInfo {
    constructor(
        public id?: number,
        public userType?: TypeUser,
        public user?: User,
        public address?: Location,
    ) {
    }
}
