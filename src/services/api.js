import axios from 'axios';

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'https://localhost:8445',
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('edupulse_token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('edupulse_token');
      localStorage.removeItem('edupulse_user');
    }
    return Promise.reject(error);
  }
);

export const endpoints = {
  login: (credentials) => api.post('/auth/login', credentials),
  students: () => api.get('/api/students'),
  student: (id) => api.get(`/api/students/${id}`),
  createStudent: (data) => api.post('/api/students', data),
  updateStudent: (id, data) => api.put(`/api/students/${id}`, data),
  deleteStudent: (id) => api.delete(`/api/students/${id}`),
  attendance: () => api.get('/api/attendance'),
  studentAttendance: (id) => api.get(`/api/attendance/student/${id}`),
  attendancePercentage: (id) => api.get(`/api/attendance/student/${id}/percentage`),
  createAttendance: (data) => api.post('/api/attendance', data),
  results: () => api.get('/api/results'),
  studentResults: (id) => api.get(`/api/results/student/${id}`),
  resultPercentage: (id) => api.get(`/api/results/student/${id}/percentage`),
  createResult: (data) => api.post('/api/results', data),
};
