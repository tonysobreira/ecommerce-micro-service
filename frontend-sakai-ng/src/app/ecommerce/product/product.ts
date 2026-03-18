import { Component, OnInit, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

import { Table, TableModule } from 'primeng/table';

export interface Product {
  id: string;
  categoryId: string;
  category: Category;
  name: string;
  description: string;
  priceCents: number;
  currency: string;
  active: boolean;
  createdAt: Date;
  updatedAt: Date;
}

export interface Category {
  id: string;
  name: string;
  createdAt: Date;
  updatedAt: Date;
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
    this.http.get<Product[]>(`${environment.apiUrl}/products`)
      .subscribe((products) => {
        this.products.set(products);
      });
  }

}
