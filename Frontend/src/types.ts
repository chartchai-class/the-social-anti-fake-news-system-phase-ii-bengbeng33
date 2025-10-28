export type Status = "FAKE" | "NOT_FAKE" | null;

export interface NewsItem {
  id: number;
  title: string;
  summary?: string;
  content: string;
  status?: Status;
  reporter: string;
  reportedAt: string;
  imageUrl?: string;
  fakeVotes?: number;
  notFakeVotes?: number;
}

export interface Comment {
  id: number;
  user: string;
  text: string;
  imageUrl?: string;
  createdAt: string;
}

export interface Vote {
  newsId: number;
  voteType: "FAKE" | "NOT_FAKE";
  userIdentifier: string;
}

export type NewsStatus = "FAKE" | "NOT_FAKE" | "UNVERIFIED";
