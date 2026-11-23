import { mediaStatusLabel, typeInfo } from '../media.js'

export function StatusBadge({ media }) {
  return (
    <span className={`badge badge--${media.status.toLowerCase()}`}>{mediaStatusLabel(media)}</span>
  )
}

export function TypeIcon({ type }) {
  const info = typeInfo(type)
  return (
    <span className={`type-icon type-icon--${type.toLowerCase()}`} title={info.label} aria-hidden>
      {info.icon}
    </span>
  )
}

export function ProgressBar({ media }) {
  const { totalProgress, length, unit } = media
  const pct = length ? Math.min(100, (totalProgress / length) * 100) : null
  return (
    <div className="progress">
      <div className="progress__text">
        <span>
          <strong>{totalProgress}</strong>
          {length != null ? ` / ${length}` : ''} {unit}
        </span>
        {pct != null && <span className="muted">{Math.round(pct)}%</span>}
      </div>
      <div className="progress__track">
        <div
          className={`progress__fill${pct == null ? ' progress__fill--unknown' : ''}`}
          style={{ width: pct == null ? (totalProgress > 0 ? '100%' : '0%') : `${pct}%` }}
        />
      </div>
    </div>
  )
}

export function Rating({ value }) {
  if (value == null) return <span className="muted">Unrated</span>
  return (
    <span className="rating" title={`${value} / 10`}>
      ★ <strong>{value}</strong>
      <span className="muted">/10</span>
    </span>
  )
}
