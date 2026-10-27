import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Students from './pages/Students';
import Attendance from './pages/Attendance';
import Results from './pages/Results';
import { useAuth } from './context/AuthContext';

function Protected({ children }) { const { isAuthenticated } = useAuth(); return isAuthenticated ? children : <Navigate to="/login" replace/>; }
function RoleRoute({ roles, children }) { const { user } = useAuth(); if (!roles.includes(String(user?.role||'').toUpperCase())) return <Navigate to="/" replace/>; return children; }
export default function App(){return <Routes><Route path="/login" element={<Login/>}/><Route path="/" element={<Protected><Layout/></Protected>}><Route index element={<Dashboard/>}/><Route path="students" element={<Protected><RoleRoute roles={['ADMIN','FACULTY']}><Students/></RoleRoute></Protected>}/><Route path="attendance" element={<Protected><Attendance/></Protected>}/><Route path="results" element={<Protected><Results/></Protected>}/></Route><Route path="*" element={<Navigate to="/" replace/>}/></Routes>;}
