import { Routes } from '@angular/router';
import { Category } from './category/category';
import { Product } from './product/product';

export default [
    { path: 'category', component: Category },
    { path: 'product', component: Product },
    { path: '**', redirectTo: '/notfound' }
] as Routes;
