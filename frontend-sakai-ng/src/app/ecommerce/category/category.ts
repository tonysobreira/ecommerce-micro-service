import { Component, OnInit, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

import { Table, TableModule } from 'primeng/table';

export interface Category {
  id: string;
  name: string;
}

@Component({
  selector: 'app-category',
  imports: [TableModule],
  templateUrl: './category.html',
  styleUrl: './category.scss'
})
export class Category implements OnInit {

  categories = signal<Category[]>([]);

  constructor(private http: HttpClient) {

  }

  ngOnInit() {
    this.http.get<Category[]>(`${environment.apiUrl}/categories`)
      .subscribe((categories) => {
        this.categories.set(categories);
      });
  }

}
