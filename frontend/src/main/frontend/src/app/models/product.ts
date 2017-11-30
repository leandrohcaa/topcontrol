export class Product {
    id: number;
    name: string;
    selectable: boolean;
    credit: number;
}
export class RequestProduct {
    product: Product;
    quantity: number = 1;
}
export class Request {
    id: number;
    datetime: Date;
    products: Array<RequestProduct>;
}