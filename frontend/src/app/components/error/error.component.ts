import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';


@Component({
  standalone: true,
  selector: 'app-error',
  templateUrl: './error.component.html',
  imports: [CommonModule, RouterLink]
})
export class ErrorComponent implements OnInit {
  
  errorType = 'general';

  constructor(private readonly route: ActivatedRoute) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const type = params.get('type');
      if (type) {
        this.errorType = type;
      }
    });
  }
}
