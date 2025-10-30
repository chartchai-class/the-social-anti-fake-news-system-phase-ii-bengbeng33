export type Status = "FAKE" | "FACT" | "UNVERIFIED" | null;

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
  voteType: "FAKE" | "FACT";
}

export interface Vote {
  id: number;
  newsId: number;
  voteType: "FAKE" | "FACT";
  userId: number;
  username: string;
}

export type NewsStatus = "FAKE" | "FACT" | "UNVERIFIED";

export interface User {
  id: number;
  name: string;
  surname: string;
  username: string;
  email: string;
  profileImagePath?: string;
  createdAt: string;
  roles?: UserRole[];
}
