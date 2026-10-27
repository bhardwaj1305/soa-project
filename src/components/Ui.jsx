import React from 'react';
import { Search, ArrowUpRight, RefreshCw, AlertCircle, CheckCircle2 } from 'lucide-react';

export function PageHeading({ eyebrow, title, description, action }) { return <div className="page-heading"><div><div className="eyebrow">{eyebrow}</div><h1>{title}</h1><p>{description}</p></div>{action && <div className="heading-action">{action}</div>}</div>; }
export function StatCard({ label, value, note, icon: Icon, tone='blue', trend }) { return <div className="stat-card"><div className="stat-top"><span>{label}</span><span className={`stat-icon ${tone}`}><Icon size={19}/></span></div><div className="stat-value">{value}</div><div className="stat-bottom"><span className="stat-note">{note}</span>{trend && <span className="stat-trend"><ArrowUpRight size={14}/>{trend}</span>}</div></div>; }
export function SearchBox({ value, onChange, placeholder='Search...' }) { return <label className="search-box"><Search size={17}/><input value={value} onChange={e=>onChange(e.target.value)} placeholder={placeholder}/></label>; }
export function Loading({ label='Loading data...' }) { return <div className="state-box"><RefreshCw className="spin" size={22}/><span>{label}</span></div>; }
export function ErrorState({ message, onRetry }) { return <div className="state-box error-state"><AlertCircle size={22}/><div><strong>Could not load data</strong><p>{message}</p>{onRetry && <button className="btn btn-secondary btn-small" onClick={onRetry}>Try again</button>}</div></div>; }
export function EmptyState({ title='No records yet', message='When records are available, they will appear here.', icon: Icon=CheckCircle2 }) { return <div className="empty-state"><div><Icon size={24}/></div><strong>{title}</strong><p>{message}</p></div>; }
export function Badge({ children, variant='neutral' }) { return <span className={`badge badge-${variant}`}>{children || '—'}</span>; }
export function TableShell({ children }) { return <div className="table-wrap"><table>{children}</table></div>; }
export function Panel({ title, subtitle, action, children, className='' }) { return <section className={`panel ${className}`}><div className="panel-heading"><div><h2>{title}</h2>{subtitle && <p>{subtitle}</p>}</div>{action}</div>{children}</section>; }
export function Field({ label, children }) { return <label className="form-field"><span>{label}</span>{children}</label>; }
