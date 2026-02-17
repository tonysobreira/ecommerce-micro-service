import { Component, OnInit, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Table, TableModule } from 'primeng/table';

export interface Product {
  id: string;
  categoryId: string;
  name: string;
  description: string;
  category: Category;
}

export interface Category {
  id: string;
  name: string;
}

@Component({
  selector: 'app-product',
  imports: [TableModule],
  templateUrl: './product.html',
  styleUrl: './product.scss'
})
export class Product implements OnInit {

  products = signal<Product[]>([]);

  constructor(private http: HttpClient) {

  }

  ngOnInit() {
    this.http.get<Product[]>('http://localhost:8080/products')
      .subscribe((products) => {
        this.products.set(products);
      });
  }

}
