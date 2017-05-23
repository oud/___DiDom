import { Country } from '../country';
export class Location {
    constructor(
        public id?: number,
        public streetAddress?: string,
        public postalCode?: string,
        public city?: string,
        public stateProvince?: string,
        public country?: Country,
    ) {
    }
}
