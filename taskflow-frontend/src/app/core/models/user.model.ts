export interface UserProfile {
  id: number;
  name: string;
  email: string;
  totalTasks: number;
  pendingTasks: number;
  inProgressTasks: number;
  completedTasks: number;
}
