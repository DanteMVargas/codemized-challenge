export interface User {
  id: number;
  name: string;
  email: string;
  createdAt: string;
}

export interface Project {
  id: number;
  name: string;
  description: string;
  owner: User;
  createdAt: string;
}

export interface Task {
  id: number;
  title: string;
  description: string;
  status: 'TODO' | 'IN_PROGRESS' | 'DONE';
  project: Project;
  assignee: User | null;
  createdAt: string;
}

export interface Comment {
  id: number;
  content: string;
  author: User;
  taskId: number;
  createdAt: string;
}