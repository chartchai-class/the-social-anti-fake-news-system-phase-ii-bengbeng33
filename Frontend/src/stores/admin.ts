import { defineStore } from "pinia";
import AdminService from "@/services/AdminService";
import type { Comment, NewsItem, User } from "@/types";

export const useAdminStore = defineStore("admin", {
  state: () => ({
    users: [] as User[],
    news: [] as NewsItem[],
    comments: [] as Comment[],
    loadingUsers: false,
    loadingNews: false,
    loadingComments: false,
  }),
  getters: {
    memberIds(state): Set<number> {
      const ids = new Set<number>();
      state.users.forEach((user) => {
        if (user.roles?.includes("MEMBER")) {
          ids.add(user.id);
        }
      });
      return ids;
    },
  },
  actions: {
    async fetchUsers() {
      this.loadingUsers = true;
      try {
        const { data } = await AdminService.getAllUsers();
        this.users = data;
      } finally {
        this.loadingUsers = false;
      }
    },
    async toggleUserMember(userId: number, makeMember: boolean) {
      const { data } = await AdminService.updateUserMember(userId, makeMember);
      const index = this.users.findIndex((user) => user.id === data.id);
      if (index >= 0) {
        this.users.splice(index, 1, data);
      } else {
        this.users.push(data);
      }
      return data;
    },
    async toggleUserVerified(userId: number, verified: boolean) {
      const { data } = await AdminService.updateUserVerified(userId, verified);
      const index = this.users.findIndex((user) => user.id === data.id);
      if (index >= 0) {
        this.users.splice(index, 1, data);
      } else {
        this.users.push(data);
      }
      return data;
    },
    async fetchNews() {
      this.loadingNews = true;
      try {
        const { data } = await AdminService.getAllNews();
        this.news = data;
      } finally {
        this.loadingNews = false;
      }
    },
    async setNewsVisibility(newsId: number, visible: boolean) {
      const { data } = await AdminService.updateNewsVisibility(newsId, visible);
      const index = this.news.findIndex((item) => item.id === data.id);
      if (index >= 0) {
        this.news.splice(index, 1, data);
      } else {
        this.news.push(data);
      }
      return data;
    },
    async fetchComments() {
      this.loadingComments = true;
      try {
        const { data } = await AdminService.getAllComments();
        this.comments = data;
      } finally {
        this.loadingComments = false;
      }
    },
    async setCommentVisibility(commentId: number, visible: boolean) {
      const { data } = await AdminService.updateCommentVisibility(
        commentId,
        visible,
      );
      const index = this.comments.findIndex((item) => item.id === data.id);
      if (index >= 0) {
        this.comments.splice(index, 1, data);
      } else {
        this.comments.push(data);
      }
      return data;
    },
  },
});
