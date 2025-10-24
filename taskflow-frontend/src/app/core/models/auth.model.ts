export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  userId: number;
  email: string;
  name: string;
}

export interface UserRegistrationResponse {
  userId: number;
  email: string;
  name: string;
  message: string;
}
