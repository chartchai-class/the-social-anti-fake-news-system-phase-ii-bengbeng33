<template>
  <!-- from-[#0b0418] via-[#150b30] to-[#230f2e] -->
  <div class="relative min-h-screen bg-gradient-to-b text-white overflow-hidden">
    <div class="pointer-events-none absolute -left-32 -top-32 h-72 w-72 rounded-full bg-orange-500/20 blur-3xl"></div>
    <div class="pointer-events-none absolute -right-32 top-1/3 h-80 w-80 rounded-full bg-purple-500/20 blur-3xl"></div>
    <div class="pointer-events-none absolute -bottom-40 left-1/2 h-96 w-96 -translate-x-1/2 rounded-full bg-pink-500/20 blur-3xl"></div>

    <div class="relative z-10 mx-auto flex w-full max-w-6xl flex-col gap-8 px-4 py-12 sm:px-8 lg:px-10">
      <header class="flex flex-col items-center gap-6 text-center">
        <div class="rounded-full border border-white/40 bg-white/10 px-6 py-2 text-sm font-semibold uppercase tracking-[0.3em] text-orange-200 shadow-lg shadow-orange-500/10">
          Management Area
        </div>
        <h1 class="text-4xl font-bold tracking-wide text-white drop-shadow-[0_6px_18px_rgba(255,255,255,0.25)] sm:text-5xl">
          Admin Dashboard
        </h1>
        <p class="max-w-2xl text-base text-gray-200 sm:text-lg">
          Manage member privileges and control which news, comments, and voices stay visible to the community. Changes apply instantly and reflect for all readers.
        </p>
        <div class="flex gap-4 rounded-full border border-white/10 bg-white/10 p-2 shadow-xl shadow-purple-500/10 backdrop-blur">
          <button
            class="w-36 rounded-full px-6 py-2 text-sm font-semibold transition-all duration-200 sm:w-40 sm:text-base"
            :class="activeTab === 'users'
              ? 'bg-orange-500 text-black shadow-lg shadow-orange-500/50'
              : 'bg-transparent text-white/80 hover:text-white'"
            @click="activeTab = 'users'"
          >
            Manage Role
          </button>
          <button
            class="w-36 rounded-full px-6 py-2 text-sm font-semibold transition-all duration-200 sm:w-40 sm:text-base"
            :class="activeTab === 'news'
              ? 'bg-orange-500 text-black shadow-lg shadow-orange-500/50'
              : 'bg-transparent text-white/80 hover:text-white'"
            @click="activeTab = 'news'"
          >
            Manage News
          </button>
          <button
            class="w-40 rounded-full px-6 py-2 text-sm font-semibold transition-all duration-200 sm:w-44 sm:text-base"
            :class="activeTab === 'comments'
              ? 'bg-orange-500 text-black shadow-lg shadow-orange-500/50'
              : 'bg-transparent text-white/80 hover:text-white'"
            @click="activeTab = 'comments'"
          >
            Manage Comments
          </button>
        </div>
      </header>

      <transition name="fade" mode="out-in">
        <section v-if="activeTab === 'users'" key="users" class="space-y-6">
          <div v-if="store.loadingUsers" class="flex justify-center">
            <div class="mt-12 h-1 w-48 overflow-hidden rounded-full bg-white/20">
              <div class="h-full w-full animate-[pulse_1.2s_ease-in-out_infinite] bg-orange-400/80"></div>
            </div>
          </div>
          <template v-else>
            <p v-if="sortedUsers.length === 0" class="rounded-2xl border border-white/10 bg-white/5 px-6 py-10 text-center text-sm text-gray-200">
              No users found. Invite new readers to start populating the community.
            </p>
            <ul v-else class="space-y-4">
              <li
                v-for="user in sortedUsers"
                :key="user.id"
                class="relative flex flex-col gap-4 rounded-3xl border border-white/10 bg-white/10 p-5 transition hover:border-orange-300/40 hover:bg-white/15 sm:flex-row sm:items-center sm:justify-between sm:p-6"
              >
                <div class="flex items-start gap-4 sm:items-center">
                  <div class="grid h-14 w-14 place-items-center rounded-full bg-white/20 text-xl font-semibold text-white/80 shadow-inner shadow-black/40">
                    {{ initials(user) }}
                  </div>
                  <div class="space-y-1">
                    <div class="flex items-center gap-2">
                      <h2 class="text-lg font-semibold text-white sm:text-xl">
                        {{ user.username || `${user.name} ${user.surname}` }}
                      </h2>
                      <VerifiedBadge :verified="isVerified(user)" size="sm" />
                    </div>
                    <p class="text-sm text-gray-300 sm:text-base">
                      {{ user.email }}
                    </p>
                    <div class="flex flex-wrap gap-2 text-xs uppercase tracking-wide text-gray-200/80">
                      <span class="rounded-full bg-white/10 px-3 py-1">
                        Joined {{ formatDate(user.createdAt) }}
                      </span>
                      <span
                        v-for="role in (user.roles ?? [])"
                        :key="`${user.id}-${role}`"
                        class="rounded-full bg-orange-400/20 px-3 py-1 text-orange-100"
                      >
                        {{ role }}
                      </span>
                    </div>
                  </div>
                </div>
                <div class="flex flex-col items-end gap-4 sm:flex-row sm:items-center">
                  <div class="flex flex-col items-end gap-2">
                    <span
                      class="text-sm font-semibold uppercase tracking-tight"
                      :class="roleLabelClass(user)"
                    >
                      {{ computeRoleLabel(user) }}
                    </span>
                    <span
                      v-if="canBeVerified(user)"
                      class="text-xs font-semibold uppercase tracking-tight"
                      :class="isVerified(user) ? 'text-green-300' : 'text-gray-400'"
                    >
                      {{ isVerified(user) ? "✓ Verified" : "Unverified" }}
                    </span>
                  </div>
                  <div class="flex flex-col gap-2 sm:flex-row">
                    <button
                      class="relative flex h-10 w-20 items-center rounded-full border border-white/10 transition-all duration-200"
                      :class="{
                        'bg-yellow-300 text-black shadow-lg shadow-yellow-400/30': isMember(user),
                        'bg-white/15 text-white': !isMember(user),
                        'opacity-60 cursor-not-allowed': isAdmin(user) || userProcessingId === user.id
                      }"
                      :disabled="isAdmin(user) || userProcessingId === user.id"
                      @click="handleToggleUser(user)"
                    >
                      <span
                        class="absolute left-1 top-1 h-8 w-8 rounded-full bg-white transition-all duration-200"
                        :class="isMember(user) ? 'translate-x-9 bg-yellow-600/80 shadow-lg shadow-yellow-500/50' : 'translate-x-0 bg-white/90 shadow-lg shadow-black/20'"
                      ></span>
                      <span class="w-full text-center text-xs font-bold tracking-wide">
                        {{ isMember(user) ? "ON" : "OFF" }}
                      </span>
                    </button>
                    <button
                      v-if="canBeVerified(user)"
                      class="relative flex h-10 w-24 items-center rounded-full border border-white/10 transition-all duration-200"
                      :class="{
                        'bg-green-400/80 text-black shadow-lg shadow-green-400/40': isVerified(user),
                        'bg-white/15 text-white': !isVerified(user),
                        'opacity-60 cursor-not-allowed': userVerifyProcessingId === user.id
                      }"
                      :disabled="userVerifyProcessingId === user.id"
                      @click="handleToggleVerify(user)"
                    >
                      <span
                        class="absolute left-1 top-1 h-8 w-8 rounded-full bg-white transition-all duration-200"
                        :class="isVerified(user) ? 'translate-x-[68px] bg-green-600/80 shadow-lg shadow-green-500/50' : 'translate-x-0 bg-white/90 shadow-lg shadow-black/20'"
                      ></span>
                      <span class="w-full text-center text-xs font-bold tracking-wide">
                        {{ isVerified(user) ? "✓ VERIFIED" : "VERIFY" }}
                      </span>
                    </button>
                  </div>
                </div>
              </li>
            </ul>
          </template>
        </section>

        <section v-else-if="activeTab === 'news'" key="news" class="space-y-6">
          <div v-if="store.loadingNews" class="flex justify-center">
            <div class="mt-12 h-1 w-48 overflow-hidden rounded-full bg-white/20">
              <div class="h-full w-full animate-[pulse_1.2s_ease-in-out_infinite] bg-orange-400/80"></div>
            </div>
          </div>
          <template v-else>
            <p v-if="sortedNews.length === 0" class="rounded-2xl border border-white/10 bg-white/5 px-6 py-10 text-center text-sm text-gray-200">
              No news articles available right now. Encourage members to submit new stories.
            </p>
            <ul v-else class="space-y-4">
              <li
                v-for="item in sortedNews"
                :key="item.id"
                class="relative flex flex-col gap-4 rounded-3xl border border-white/10 bg-white/10 p-5 transition hover:border-orange-300/40 hover:bg-white/15 sm:flex-row sm:items-center sm:justify-between sm:p-6"
              >
                <div class="space-y-3">
                  <div class="flex flex-col gap-2">
                    <h3 class="text-xl font-semibold text-white">
                      {{ item.title }}
                    </h3>
                    <p class="max-w-2xl text-sm text-gray-200 sm:text-base">
                      {{ item.summary ?? truncate(item.content, 180) }}
                    </p>
                  </div>
                  <div class="flex flex-wrap gap-3 text-xs uppercase tracking-wide text-gray-300/90">
                    <span class="flex items-center gap-1.5 rounded-full bg-white/10 px-3 py-1">
                      {{ item.reporter }}
                      <VerifiedBadge :verified="item.reporterVerified" size="xs" />
                    </span>
                    <span class="rounded-full bg-white/10 px-3 py-1">
                      {{ formatDate(item.reportedAt) }}
                    </span>
                    <span class="rounded-full bg-orange-400/20 px-3 py-1 text-orange-100">
                      {{ item.status ?? "UNVERIFIED" }}
                    </span>
                  </div>
                </div>
                <div class="flex flex-col items-end gap-4 sm:items-end">
                  <span
                    class="rounded-full px-4 py-1 text-sm font-semibold tracking-wide"
                    :class="isVisible(item) ? 'bg-green-400/20 text-green-200' : 'bg-red-500/20 text-red-200'"
                  >
                    {{ isVisible(item) ? "Visible" : "Invisible" }}
                  </span>
                  <button
                    class="relative flex h-10 w-24 items-center justify-center gap-2 rounded-full border border-white/10 text-sm font-semibold uppercase tracking-wide transition-all duration-200"
                    :class="{
                      'bg-green-400/80 text-black shadow-lg shadow-green-400/40': isVisible(item),
                      'bg-white/15 text-white': !isVisible(item),
                      'opacity-60 cursor-not-allowed': newsProcessingId === item.id
                    }"
                    :disabled="newsProcessingId === item.id"
                    @click="handleToggleNews(item)"
                  >
                    <span class="relative h-5 w-8">
                      <svg
                        v-if="isVisible(item)"
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        class="h-5 w-5 fill-current"
                      >
                        <path
                          d="M12 5c-6.5 0-9.27 6.27-9.38 6.52a1 1 0 0 0 0 .96C2.73 12.73 5.5 19 12 19s9.27-6.27 9.38-6.52a1 1 0 0 0 0-.96C21.27 11.27 18.5 5 12 5Zm0 12c-4.65 0-6.97-4.21-7.35-5 .38-.79 2.7-5 7.35-5s6.97 4.21 7.35 5c-.38.79-2.7 5-7.35 5Zm0-8a3 3 0 1 0 3 3 3 3 0 0 0-3-3Zm0 4a1 1 0 1 1 1-1 1 1 0 0 1-1 1Z"
                        />
                      </svg>
                      <svg
                        v-else
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        class="h-5 w-5 fill-current"
                      >
                        <path
                          d="M21.78 2.22a1 1 0 0 0-1.56 0l-3.13 3.14A11.73 11.73 0 0 0 12 5C5.5 5 2.73 11.27 2.62 11.52a1 1 0 0 0 0 .96 15.73 15.73 0 0 0 4.11 5.22l-3 3a1 1 0 0 0 1.42 1.42l18-18a1 1 0 0 0 0-1.42ZM12 7a4.94 4.94 0 0 1 3.43 1.34l-1.46 1.46a2 2 0 0 0-2.77 2.77l-1.46 1.46A3 3 0 0 1 12 9Zm0 10c-4.65 0-6.97-4.21-7.35-5a13.94 13.94 0 0 1 3.61-4.16l1.55 1.55a3.94 3.94 0 0 0-1.03 2.61 4 4 0 0 0 6.52 3l1.46 1.46A8.79 8.79 0 0 1 12 17Zm9.38-4.52C21.27 12.73 18.5 19 12 19a8.75 8.75 0 0 1-1.56-.14l2.13-2.13a3.94 3.94 0 0 0 4.24-4.24l3.02-3a16.19 16.19 0 0 1 1.55 2.03 1 1 0 0 1 0 .96Z"
                        />
                      </svg>
                    </span>
                    {{ isVisible(item) ? "Hide" : "Show" }}
                  </button>
                </div>
              </li>
            </ul>
          </template>
        </section>

        <section v-else key="comments" class="space-y-6">
          <div v-if="store.loadingComments" class="flex justify-center">
            <div class="mt-12 h-1 w-48 overflow-hidden rounded-full bg-white/20">
              <div class="h-full w-full animate-[pulse_1.2s_ease-in-out_infinite] bg-orange-400/80"></div>
            </div>
          </div>
          <template v-else>
            <p v-if="sortedComments.length === 0" class="rounded-2xl border border-white/10 bg-white/5 px-6 py-10 text-center text-sm text-gray-200">
              No comments available yet. Encourage members to share their thoughts.
            </p>
            <ul v-else class="space-y-4">
              <li
                v-for="comment in sortedComments"
                :key="comment.id"
                class="relative flex flex-col gap-4 rounded-3xl border border-white/10 bg-white/10 p-5 transition hover:border-orange-300/40 hover:bg-white/15 sm:flex-row sm:items-center sm:justify-between sm:p-6"
              >
                <div class="space-y-3">
                  <div class="flex flex-col gap-2">
                    <h3 class="text-lg font-semibold text-white">
                      {{ comment.newsTitle ?? 'Untitled news' }}
                    </h3>
                    <p class="text-sm text-gray-200/90">
                      {{ comment.text || '— No comment text provided —' }}
                    </p>
                  </div>
                  <div class="flex flex-wrap gap-3 text-xs uppercase tracking-wide text-gray-300/90">
                    <span class="flex items-center gap-1.5 rounded-full bg-white/10 px-3 py-1">
                      {{ comment.username }}
                      <VerifiedBadge :verified="comment.userVerified" size="xs" />
                    </span>
                    <span class="rounded-full bg-white/10 px-3 py-1">
                      {{ formatDateTime(comment.createdAt) }}
                    </span>
                    <span
                      class="rounded-full px-3 py-1"
                      :class="comment.voteType === 'FAKE'
                        ? 'bg-red-500/20 text-red-200'
                        : 'bg-green-500/20 text-green-200'"
                    >
                      {{ comment.voteType }}
                    </span>
                  </div>
                </div>
                <div class="flex flex-col items-end justify-between gap-4 sm:items-end">
                  <span
                    class="rounded-full px-4 py-1 text-sm font-semibold tracking-wide"
                    :class="isCommentVisible(comment) ? 'bg-green-400/20 text-green-200' : 'bg-red-500/20 text-red-200'"
                  >
                    {{ isCommentVisible(comment) ? "Visible" : "Hidden" }}
                  </span>
                  <button
                    class="relative flex h-10 w-24 items-center justify-center gap-2 rounded-full border border-white/10 text-sm font-semibold uppercase tracking-wide transition-all duration-200"
                    :class="{
                      'bg-green-400/80 text-black shadow-lg shadow-green-400/40': isCommentVisible(comment),
                      'bg-white/15 text-white': !isCommentVisible(comment),
                      'opacity-60 cursor-not-allowed': commentProcessingId === comment.id
                    }"
                    :disabled="commentProcessingId === comment.id"
                    @click="handleToggleComment(comment)"
                  >
                    <span class="relative h-5 w-8">
                      <svg
                        v-if="isCommentVisible(comment)"
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        class="h-5 w-5 fill-current"
                      >
                        <path
                          d="M5 5a1 1 0 0 0-.92.62l-3 7a1 1 0 0 0 .08.9l3 5A1 1 0 0 0 5 19h14a1 1 0 0 0 .86-.5l3-5a1 1 0 0 0 .08-.9l-3-7A1 1 0 0 0 19 5Zm1.38 2h11.24l2.14 5-2.14 3.5H6.38L4.24 12Z"
                        />
                      </svg>
                      <svg
                        v-else
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        class="h-5 w-5 fill-current"
                      >
                        <path
                          d="M4.21 3.21a1 1 0 0 0-1.42 1.42l1.75 1.75-2.46 5.72a1 1 0 0 0 .08.9l3 5A1 1 0 0 0 6 19h11.59l2.2 2.2a1 1 0 0 0 1.42-1.42ZM6.38 17 4.24 13 6 9l3.76 3.76L12 15l1.24 2Zm13.62-.38L11.38 8l-.58-.58L16.76 7l2.14 5Z"
                        />
                      </svg>
                    </span>
                    {{ isCommentVisible(comment) ? "Hide" : "Show" }}
                  </button>
                </div>
              </li>
            </ul>
          </template>
        </section>
      </transition>

      <div
        v-if="errorMessage"
        class="rounded-2xl border border-red-400/50 bg-red-900/30 px-4 py-3 text-sm text-red-100 shadow-lg shadow-red-900/30"
      >
        {{ errorMessage }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import type { AxiosError } from "axios";
import { useAdminStore } from "@/stores/admin";
import type { Comment, NewsItem, User } from "@/types";
import VerifiedBadge from "@/components/VerifiedBadge.vue";

type TabKey = "users" | "news" | "comments";

const activeTab = ref<TabKey>("users");
const errorMessage = ref<string | null>(null);
const userProcessingId = ref<number | null>(null);
const userVerifyProcessingId = ref<number | null>(null);
const newsProcessingId = ref<number | null>(null);
const commentProcessingId = ref<number | null>(null);

const store = useAdminStore();

const sortedUsers = computed(() => {
  return [...store.users].sort((a, b) => {
    const aDate = a.createdAt ?? "";
    const bDate = b.createdAt ?? "";
    return bDate.localeCompare(aDate);
  });
});

const sortedNews = computed(() => {
  return [...store.news].sort((a, b) => {
    const aDate = a.reportedAt ?? "";
    const bDate = b.reportedAt ?? "";
    return bDate.localeCompare(aDate);
  });
});

const sortedComments = computed(() => {
  return [...store.comments].sort((a, b) => {
    const aDate = a.createdAt ?? "";
    const bDate = b.createdAt ?? "";
    return bDate.localeCompare(aDate);
  });
});

onMounted(async () => {
  try {
    if (store.users.length === 0) {
      await store.fetchUsers();
    }
    if (store.news.length === 0) {
      await store.fetchNews();
    }
    if (store.comments.length === 0) {
      await store.fetchComments();
    }
  } catch (err) {
    errorMessage.value = toMessage(err);
  }
});

watch(activeTab, async (tab) => {
  if (tab === "comments") {
    try {
      await store.fetchComments();
    } catch (err) {
      errorMessage.value = toMessage(err);
    }
  }
});

function toMessage(err: unknown): string {
  if (!err) return "Unexpected error occurred.";
  const axiosErr = err as AxiosError<{ message?: string; error?: string }>;
  return (
    axiosErr.response?.data?.message ??
    axiosErr.response?.data?.error ??
    axiosErr.message ??
    "Unexpected error occurred."
  );
}

function initials(user: User): string {
  const name = [user.name, user.surname]
    .filter((part) => !!part && part.trim().length > 0)
    .map((part) => part.trim()[0]?.toUpperCase() ?? "");
  if (name.length === 0 && user.username) {
    return user.username
      .split(" ")
      .filter(Boolean)
      .slice(0, 2)
      .map((segment) => segment[0]?.toUpperCase() ?? "")
      .join("");
  }
  return name.join("") || "U";
}

function isMember(user: User): boolean {
  return user.roles?.includes("MEMBER") ?? false;
}

function isAdmin(user: User): boolean {
  return user.roles?.includes("ADMIN") ?? false;
}

function isVerified(user: User): boolean {
  return user.verified ?? false;
}

function canBeVerified(user: User): boolean {
  return isMember(user) || isAdmin(user);
}

function computeRoleLabel(user: User): string {
  if (isAdmin(user)) return "Administrator";
  if (isMember(user)) return "Member";
  return "Reader";
}

function roleLabelClass(user: User): string {
  if (isAdmin(user)) return "text-orange-200";
  if (isMember(user)) return "text-yellow-200";
  return "text-gray-200";
}

function formatDate(iso?: string | null): string {
  if (!iso) return "Unknown";
  const date = new Date(iso);
  if (Number.isNaN(date.getTime())) return iso;
  return date.toLocaleString(undefined, { dateStyle: "medium" });
}

function formatDateTime(iso?: string | null): string {
  if (!iso) return "Unknown";
  const date = new Date(iso);
  if (Number.isNaN(date.getTime())) return iso;
  return date.toLocaleString(undefined, {
    dateStyle: "medium",
    timeStyle: "short",
  });
}

function truncate(text: string | undefined, limit: number): string {
  if (!text) return "";
  if (text.length <= limit) return text;
  return `${text.slice(0, limit)}…`;
}

function isVisible(news: NewsItem): boolean {
  return news.visible ?? true;
}

async function handleToggleUser(user: User) {
  if (isAdmin(user)) return;
  userProcessingId.value = user.id;
  errorMessage.value = null;
  try {
    await store.toggleUserMember(user.id, !isMember(user));
  } catch (err) {
    errorMessage.value = toMessage(err);
  } finally {
    userProcessingId.value = null;
  }
}

async function handleToggleVerify(user: User) {
  if (!canBeVerified(user)) return;
  userVerifyProcessingId.value = user.id;
  errorMessage.value = null;
  try {
    await store.toggleUserVerified(user.id, !isVerified(user));
  } catch (err) {
    errorMessage.value = toMessage(err);
  } finally {
    userVerifyProcessingId.value = null;
  }
}

async function handleToggleNews(news: NewsItem) {
  newsProcessingId.value = news.id;
  errorMessage.value = null;
  try {
    const visible = isVisible(news);
    await store.setNewsVisibility(news.id, !visible);
  } catch (err) {
    errorMessage.value = toMessage(err);
  } finally {
    newsProcessingId.value = null;
  }
}

function isCommentVisible(comment: Comment): boolean {
  return comment.visible ?? true;
}

async function handleToggleComment(comment: Comment) {
  commentProcessingId.value = comment.id;
  errorMessage.value = null;
  try {
    const visible = isCommentVisible(comment);
    const updated = await store.setCommentVisibility(comment.id, !visible);
    await store.fetchNews();
    if (updated.newsId) {
      window.dispatchEvent(
        new CustomEvent("admin-comment-updated", {
          detail: { newsId: updated.newsId },
        }),
      );
    }
  } catch (err) {
    errorMessage.value = toMessage(err);
  } finally {
    commentProcessingId.value = null;
  }
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
