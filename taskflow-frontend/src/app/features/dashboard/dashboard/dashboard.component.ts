import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { TaskService } from '../../../core/services/task.service';
import { CreateTaskRequest, Task, TaskPriority, TaskStatus } from '../../../core/models/task.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  readonly statusOptions: Array<{ value: TaskStatus; label: string }> = [
    { value: 'POR_HACER', label: 'Por hacer' },
    { value: 'EN_PROGRESO', label: 'En progreso' },
    { value: 'COMPLETADA', label: 'Completada' }
  ];

  readonly priorities: Array<{ value: TaskPriority; label: string }> = [
    { value: 'ALTA', label: 'Alta' },
    { value: 'MEDIA', label: 'Media' },
    { value: 'BAJA', label: 'Baja' }
  ];

  tasks: Task[] = [];
  filteredStatus: TaskStatus | 'ALL' = 'ALL';
  selectedTask: Task | null = null;
  isLoading = false;
  isSubmitting = false;
  errorMessage = '';
  successMessage = '';

  form = this.fb.nonNullable.group({
    title: ['', [Validators.required, Validators.maxLength(120)]],
    description: ['', [Validators.maxLength(500)]],
    priority: ['MEDIA' as TaskPriority, Validators.required]
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly taskService: TaskService
  ) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  get columns() {
    return this.statusOptions.filter(column => this.filteredStatus === 'ALL' || column.value === this.filteredStatus);
  }

  tasksByStatus(status: TaskStatus): Task[] {
    return this.tasks.filter(task => task.status === status);
  }

  onFilterChange(status: TaskStatus | 'ALL'): void {
    this.filteredStatus = status;
  }

  reloadTasks(): void {
    this.loadTasks();
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = this.form.getRawValue() as CreateTaskRequest;
    this.isSubmitting = true;

    if (this.selectedTask) {
      this.taskService
        .updateTask(this.selectedTask.id, payload)
        .pipe(finalize(() => (this.isSubmitting = false)))
        .subscribe({
          next: (updatedTask) => {
            this.replaceTask(updatedTask);
            this.successMessage = 'Tarea actualizada correctamente.';
            this.resetForm();
          },
          error: (error) => this.handleError(error)
        });
    } else {
      this.taskService
        .createTask(payload)
        .pipe(finalize(() => (this.isSubmitting = false)))
        .subscribe({
          next: (createdTask) => {
            this.tasks = [createdTask, ...this.tasks];
            this.successMessage = 'Tarea creada correctamente.';
            this.resetForm();
          },
          error: (error) => this.handleError(error)
        });
    }
  }

  editTask(task: Task): void {
    this.selectedTask = task;
    this.form.patchValue({
      title: task.title,
      description: task.description,
      priority: task.priority
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelEdit(): void {
    this.resetForm();
  }

  updateStatus(task: Task, status: TaskStatus | string): void {
    const normalizedStatus = status as TaskStatus;

    if (task.status === normalizedStatus) {
      return;
    }

    this.taskService.updateStatus(task.id, normalizedStatus).subscribe({
      next: (updatedTask) => this.replaceTask(updatedTask),
      error: (error) => this.handleError(error)
    });
  }

  advanceStatus(task: Task): void {
    const next = this.getNextStatus(task.status);
    if (next) {
      this.updateStatus(task, next);
    }
  }

  revertStatus(task: Task): void {
    const previous = this.getPreviousStatus(task.status);
    if (previous) {
      this.updateStatus(task, previous);
    }
  }

  deleteTask(task: Task): void {
    if (!confirm('¿Seguro que deseas eliminar esta tarea?')) {
      return;
    }

    this.taskService.deleteTask(task.id).subscribe({
      next: () => {
        this.tasks = this.tasks.filter(t => t.id !== task.id);
        this.successMessage = 'Tarea eliminada correctamente.';
      },
      error: (error) => this.handleError(error)
    });
  }

  priorityBadge(priority: TaskPriority): string {
    switch (priority) {
      case 'ALTA':
        return 'bg-danger';
      case 'MEDIA':
        return 'bg-warning text-dark';
      default:
        return 'bg-secondary';
    }
  }

  priorityLabel(priority: TaskPriority): string {
    return this.priorities.find(item => item.value === priority)?.label ?? priority;
  }

  statusBadge(status: TaskStatus): string {
    switch (status) {
      case 'POR_HACER':
        return 'bg-secondary';
      case 'EN_PROGRESO':
        return 'bg-info text-dark';
      case 'COMPLETADA':
        return 'bg-success';
      default:
        return 'bg-secondary';
    }
  }

  statusLabel(status: TaskStatus): string {
    return this.statusOptions.find(item => item.value === status)?.label ?? status;
  }

  private loadTasks(): void {
    this.errorMessage = '';
    this.successMessage = '';
    this.isLoading = true;
    this.taskService
      .getTasks()
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (tasks) => (this.tasks = tasks),
        error: (error) => this.handleError(error)
      });
  }

  private resetForm(): void {
    this.form.reset({
      title: '',
      description: '',
      priority: 'MEDIA' as TaskPriority
    });
    this.form.markAsPristine();
    this.form.markAsUntouched();
    this.selectedTask = null;
  }

  private getNextStatus(status: TaskStatus): TaskStatus | null {
    const order: TaskStatus[] = ['POR_HACER', 'EN_PROGRESO', 'COMPLETADA'];
    const index = order.indexOf(status);
    return index >= 0 && index < order.length - 1 ? order[index + 1] : null;
  }

  private getPreviousStatus(status: TaskStatus): TaskStatus | null {
    const order: TaskStatus[] = ['POR_HACER', 'EN_PROGRESO', 'COMPLETADA'];
    const index = order.indexOf(status);
    return index > 0 ? order[index - 1] : null;
  }

  private replaceTask(task: Task): void {
    this.tasks = this.tasks.map(existing => (existing.id === task.id ? task : existing));
  }

  private handleError(error: unknown): void {
    console.error(error);
    this.errorMessage = (error as any)?.error?.message ?? 'Ocurrió un error. Intenta nuevamente.';
  }

}
