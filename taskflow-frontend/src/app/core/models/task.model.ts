export type TaskPriority = 'ALTA' | 'MEDIA' | 'BAJA';
export type TaskStatus = 'POR_HACER' | 'EN_PROGRESO' | 'COMPLETADA';

export interface Task {
  id: number;
  title: string;
  description: string;
  priority: TaskPriority;
  status: TaskStatus;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateTaskRequest {
  title: string;
  description: string;
  priority: TaskPriority;
}

export interface UpdateTaskRequest {
  title: string;
  description: string;
  priority: TaskPriority;
}

export interface UpdateTaskStatusRequest {
  status: TaskStatus;
}
