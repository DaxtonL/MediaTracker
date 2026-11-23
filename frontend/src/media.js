// Mirrors the backend MediaType / Status enums and the display rules
// from the original Swing app (Media.listMediaInfo / mediaTypeStrings).

export const MEDIA_TYPES = {
  MOVIE: { label: 'Movie', verb: 'Watching', unit: 'minutes', icon: '🎬' },
  SHOW: { label: 'Show', verb: 'Watching', unit: 'episodes', icon: '📺' },
  GAME: { label: 'Game', verb: 'Playing', unit: 'hours', icon: '🎮' },
  BOOK: { label: 'Book', verb: 'Reading', unit: 'pages', icon: '📖' },
  MANGA: { label: 'Manga', verb: 'Reading', unit: 'chapters', icon: '🗯️' },
}

export const STATUSES = ['WAITLIST', 'VIEWING', 'FINISHED', 'HOLD', 'DROPPED']

const STATUS_LABELS = {
  WAITLIST: 'Waitlist',
  VIEWING: 'Viewing',
  FINISHED: 'Finished',
  HOLD: 'On hold',
  DROPPED: 'Dropped',
}

// Generic label, used where no media type is known (e.g. filter tabs)
export function statusLabel(status) {
  return STATUS_LABELS[status] ?? status
}

// Label for a specific media: VIEWING becomes "Watching"/"Playing"/"Reading"
export function mediaStatusLabel(media) {
  if (media.status === 'VIEWING') return MEDIA_TYPES[media.type]?.verb ?? 'Viewing'
  return statusLabel(media.status)
}

export function typeInfo(type) {
  return MEDIA_TYPES[type] ?? { label: type, verb: 'Viewing', unit: '', icon: '•' }
}

export function todayIso() {
  const d = new Date()
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`
}

export function formatDate(iso) {
  const [y, m, d] = iso.split('-').map(Number)
  return new Date(y, m - 1, d).toLocaleDateString(undefined, {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  })
}
