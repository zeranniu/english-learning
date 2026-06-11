import { httpGet, httpPost, httpPut, httpDelete } from '@/http/http'

// Auth
export const authApi = {
  login: (data: { username: string; password: string }) => httpPost<any>('/auth/login', data),
  register: (data: { username: string; password: string; nickname: string }) => httpPost<any>('/auth/register', data),
  getProfile: () => httpGet<any>('/auth/profile'),
  updateProfile: (data: Record<string, string>) => httpPut<any>('/auth/profile', data),
}

// Home
export const homeApi = {
  getStats: () => httpGet<any>('/home/stats'),
  getTasks: () => httpGet<any>('/home/tasks'),
  getBadges: () => httpGet<any>('/home/badges'),
}

// Vocab
export const vocabApi = {
  getWords: () => httpGet<any>('/vocab/words'),
  learnWord: (data: { wordId: number; known: boolean }) => httpPost<any>('/vocab/learn', data),
  speakWord: (word: string) => httpPost<any>('/vocab/speak', { word }),
}

// Listening
export const listeningApi = {
  getLessons: () => httpGet<any>('/listening/lessons'),
  getLessonDetail: (id: number) => httpGet<any>(`/listening/lesson/${id}`),
  answerQuestion: (data: { questionId: number; answer: string }) => httpPost<any>('/listening/answer', data),
}

// Reading
export const readingApi = {
  getPassages: () => httpGet<any>('/reading/passages'),
  getPassageDetail: (id: number) => httpGet<any>(`/reading/passage/${id}`),
  answerQuestion: (data: { questionId: number; answerIndex: number }) => httpPost<any>('/reading/answer', data),
}

// Grammar
export const grammarApi = {
  getQuestions: () => httpGet<any>('/grammar/questions'),
  answerQuestion: (data: { questionId: number; selectedIndex: number }) => httpPost<any>('/grammar/answer', data),
}

// PK
export const pkApi = {
  getRanking: () => httpGet<any>('/pk/ranking'),
  startPK: () => httpPost<any>('/pk/start'),
}

// Growth
export const growthApi = {
  getInfo: () => httpGet<any>('/growth/info'),
}

// Mistakes
export const mistakeApi = {
  getList: (category: string) => httpGet<any>('/mistakes/list', { category }),
}

// Notifications
export const notificationApi = {
  getList: () => httpGet<any>('/notifications/list'),
  markAsRead: (id: number) => httpPut<any>(`/notifications/${id}/read`),
}

// Reminders
export const reminderApi = {
  getList: () => httpGet<any>('/reminders/list'),
  toggle: (data: { id: number; enabled: boolean }) => httpPut<any>('/reminders/toggle', data),
}

// Feedback
export const feedbackApi = {
  submit: (data: { feedbackType: string; content: string; contact: string }) => httpPost<any>('/feedback/submit', data),
}

// Privacy
export const privacyApi = {
  getSettings: () => httpGet<any>('/privacy/settings'),
}

// About
export const aboutApi = {
  getInfo: () => httpGet<any>('/about/info'),
}
