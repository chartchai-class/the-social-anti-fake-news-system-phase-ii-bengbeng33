export type Status = "FAKE" | "NOT_FAKE" | "UNVERIFIED" | null;

export type UserRole = "READER" | "MEMBER" | "ADMIN";

export interface NewsItem {
  id: number;
  title: string;
  summary?: string;
  content: string;
  status?: Status;
  currentStatus?: Status;
  reporter: string;
  reportedAt: string;
  imageUrl?: string;
  fakeVotes?: number;
  notFakeVotes?: number;
}

export interface Comment {
  id: number;
  userId: number;
  username: string;
  text: string;
  imageUrl?: string;
  createdAt: string;
  voteType: "FAKE" | "NOT_FAKE";
}

export interface Vote {
  id: number;
  newsId: number;
  voteType: "FAKE" | "NOT_FAKE";
  userId: number;
  username: string;
}

export type NewsStatus = "FAKE" | "NOT_FAKE" | "UNVERIFIED";

export interface User {
  id: number;
  name: string;
  surname: string;
  username: string;
  email: string;
  createdAt: string;
  roles?: UserRole[];
}
