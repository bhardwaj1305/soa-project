import React from 'react';
import { NavLink, Outlet, useLocation } from 'react-router-dom';
import { Activity, BookOpenCheck, CalendarCheck2, ChevronDown, GraduationCap, LayoutDashboard, LogOut, Menu, Settings2, Users, X, Bell } from 'lucide-react';
import { useState } from 'react';
import { useAuth } from '../context/AuthContext';

const nav = [
  { to: '/', label: 'Overview', icon: LayoutDashboard, roles: ['ADMIN','FACULTY','STUDENT'] },
  { to: '/students', label: 'Students', icon: Users, roles: ['ADMIN','FACULTY'] },
  { to: '/attendance', label: 'Attendance', icon: CalendarCheck2, roles: ['ADMIN','FACULTY','STUDENT'] },
  { to: '/results', label: 'Results & grades', icon: BookOpenCheck, roles: ['ADMIN','FACULTY','STUDENT'] },
];

export default function Layout() {
  const { user, logout } = useAuth();
  const [mobileOpen, setMobileOpen] = useState(false);
  const location = useLocation();
  const title = nav.find(n => n.to === location.pathname)?.label || 'EduPulse';
  const role = String(user?.role || 'USER').toUpperCase();
  return <div className="app-shell">
    {mobileOpen && <button className="mobile-scrim" aria-label="Close menu" onClick={() => setMobileOpen(false)} />}
    <aside className={`sidebar ${mobileOpen ? 'sidebar-open' : ''}`}>
      <div className="brand"><div className="brand-mark"><GraduationCap size={24}/></div><div><strong>EduPulse</strong><span>ACADEMIC PORTAL</span></div><button className="icon-button mobile-close" onClick={() => setMobileOpen(false)}><X size={18}/></button></div>
      <div className="workspace-label">WORKSPACE</div>
      <nav className="side-nav">{nav.filter(item => item.roles.includes(role)).map(item => <NavLink key={item.to} to={item.to} end={item.to === '/'} onClick={() => setMobileOpen(false)} className={({isActive}) => `nav-link ${isActive ? 'active' : ''}`}><item.icon size={19}/><span>{item.label}</span>{item.to === '/' && <span className="nav-dot"/>}</NavLink>)}</nav>
      <div className="sidebar-bottom"><div className="help-card"><div className="help-icon"><Activity size={17}/></div><strong>Keep moving forward</strong><p>Your progress, all in one place.</p><div className="help-progress"><span/></div></div><button className="nav-link settings-link"><Settings2 size={18}/><span>Workspace settings</span></button><div className="profile-row"><div className="avatar">{(user?.username || 'U').slice(0,1).toUpperCase()}</div><div className="profile-copy"><strong>{user?.username || 'Guest user'}</strong><span>{role.toLowerCase()}</span></div><button className="icon-button" title="Sign out" onClick={logout}><LogOut size={17}/></button></div></div>
    </aside>
    <main className="main-area"><header className="topbar"><button className="icon-button mobile-menu" onClick={() => setMobileOpen(true)}><Menu size={21}/></button><div className="breadcrumb">Workspace <span>/</span> <strong>{title}</strong></div><div className="topbar-actions"><div className="status-pill"><span/> Services portal</div><button className="icon-button notification-button" aria-label="Notifications"><Bell size={19}/><i/></button><div className="top-divider"/><div className="top-user"><div className="avatar avatar-small">{(user?.username || 'U').slice(0,1).toUpperCase()}</div><div><strong>{user?.username || 'User'}</strong><span>{role}</span></div><ChevronDown size={15}/></div></div></header><div className="page-content"><Outlet/></div><footer className="footer"><span>© 2026 EduPulse</span><span>Student Information & Academic Performance Platform</span><span className="footer-status"><i/> Portal interface</span></footer></main>
  </div>;
}
