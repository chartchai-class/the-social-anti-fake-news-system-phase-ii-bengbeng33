<template>
  <!-- from-[#0b0418] via-[#150b30] to-[#230f2e] -->
  <div class="relative min-h-screen bg-gradient-to-b text-white overflow-hidden">
    <div class="pointer-events-none absolute -left-32 -top-32 h-72 w-72 rounded-full bg-orange-500/20 blur-3xl"></div>
    <div class="pointer-events-none absolute -right-32 top-1/3 h-80 w-80 rounded-full bg-purple-500/20 blur-3xl"></div>
    <div class="pointer-events-none absolute -bottom-40 left-1/2 h-96 w-96 -translate-x-1/2 rounded-full bg-pink-500/20 blur-3xl"></div>

    <div class="relative z-10 mx-auto flex w-full max-w-6xl flex-col gap-6 px-3 py-8 sm:gap-8 sm:px-8 sm:py-12 lg:px-10">
      <header class="flex flex-col items-center gap-3 text-center sm:gap-6">
        <div class="rounded-full border border-white/40 bg-white/10 px-2 py-1 text-[10px] font-semibold uppercase tracking-[0.15em] text-orange-200 shadow-lg shadow-orange-500/10 sm:px-6 sm:py-2 sm:text-sm sm:tracking-[0.3em]">
          Management Area
        </div>
        <h1 class="text-2xl font-bold tracking-wide text-white drop-shadow-[0_6px_18px_rgba(255,255,255,0.25)] sm:text-4xl lg:text-5xl">
          Admin Dashboard
        </h1>
        <p class="max-w-2xl px-3 text-xs text-gray-200 leading-relaxed sm:px-0 sm:text-base lg:text-lg">
          Manage member privileges and control which news, comments, and voices stay visible to the community. Changes apply instantly and reflect for all readers.
        </p>
        <div class="flex flex-wrap justify-center gap-2 rounded-full border border-white/10 bg-white/10 p-1.5 shadow-xl shadow-purple-500/10 backdrop-blur sm:flex-nowrap sm:gap-4 sm:p-2">
          <button
            class="w-28 rounded-full px-3 py-1.5 text-[11px] font-semibold transition-all duration-200 sm:w-40 sm:px-6 sm:py-2 sm:text-base"
            :class="activeTab === 'users'
              ? 'bg-orange-500 text-black shadow-lg shadow-orange-500/50'
              : 'bg-transparent text-white/80 hover:text-white'"
            @click="activeTab = 'users'"
          >
            Manage Role
          </button>
          <button
            class="w-28 rounded-full px-3 py-1.5 text-[11px] font-semibold transition-all duration-200 sm:w-40 sm:px-6 sm:py-2 sm:text-base"
            :class="activeTab === 'news'
              ? 'bg-orange-500 text-black shadow-lg shadow-orange-500/50'
              : 'bg-transparent text-white/80 hover:text-white'"
            @click="activeTab = 'news'"
          >
            Manage News
          </button>
          <button
            class="w-32 rounded-full px-3 py-1.5 text-[11px] font-semibold transition-all duration-200 sm:w-44 sm:px-6 sm:py-2 sm:text-base"
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
            <p v-if="sortedUsers.length === 0" class="rounded-xl border border-white/10 bg-white/5 px-4 py-8 text-center text-xs text-gray-200 sm:rounded-2xl sm:px-6 sm:py-10 sm:text-sm">
              No users found. Invite new readers to start populating the community.
            </p>
            <ul v-else class="space-y-3 sm:space-y-4">
              <li
                v-for="user in sortedUsers"
                :key="user.id"
                class="relative flex flex-col gap-3 rounded-2xl border border-white/10 bg-white/10 p-4 transition hover:border-orange-300/40 hover:bg-white/15 sm:gap-4 sm:rounded-3xl sm:flex-row sm:items-center sm:justify-between sm:p-6"
              >
                <div class="flex items-start gap-3 sm:gap-4 sm:items-center">
                  <div class="grid h-12 w-12 flex-shrink-0 place-items-center rounded-full bg-white/20 text-lg font-semibold text-white/80 shadow-inner shadow-black/40 sm:h-14 sm:w-14 sm:text-xl">
                    {{ initials(user) }}
                  </div>
                  <div class="space-y-1 flex-1 min-w-0">
                    <div class="flex items-center gap-2 flex-wrap">
                      <h2 class="text-base font-semibold text-white sm:text-lg lg:text-xl break-words">
                        {{ user.username || `${user.name} ${user.surname}` }}
                      </h2>
                      <VerifiedBadge :verified="isVerified(user)" size="sm" />
                    </div>
                    <p class="text-xs text-gray-300 sm:text-sm lg:text-base break-all">
                      {{ user.email }}
                    </p>
                    <div class="flex flex-wrap gap-1.5 sm:gap-2 text-[10px] uppercase tracking-wide text-gray-200/80 sm:text-xs">
                      <span class="rounded-full bg-white/10 px-2 py-0.5 sm:px-3 sm:py-1">
                        Joined {{ formatDate(user.createdAt) }}
                      </span>
                      <span
                        v-for="role in (user.roles ?? [])"
                        :key="`${user.id}-${role}`"
                        class="rounded-full bg-orange-400/20 px-2 py-0.5 text-orange-100 sm:px-3 sm:py-1"
                      >
                        {{ role }}
                      </span>
                    </div>
                  </div>
                </div>
                <div class="flex flex-row items-center justify-between gap-3 sm:flex-col sm:items-end sm:gap-4">
                  <div class="flex flex-row items-center gap-2 sm:flex-col sm:items-end sm:gap-2">
                    <span
                      class="text-xs font-semibold uppercase tracking-tight whitespace-nowrap sm:text-sm"
                      :class="roleLabelClass(user)"
                    >
                      {{ computeRoleLabel(user) }}
                    </span>
                    <span
                      v-if="canBeVerified(user)"
                      class="text-[10px] font-semibold uppercase tracking-tight whitespace-nowrap sm:text-xs"
                      :class="isVerified(user) ? 'text-green-300' : 'text-gray-400'"
                    >
                      {{ isVerified(user) ? "✓ Verified" : "Unverified" }}
                    </span>
                  </div>
                  <div class="flex flex-row gap-2">
                    <button
                      class="relative flex h-9 w-[72px] items-center rounded-full border border-white/10 transition-all duration-200 sm:h-10 sm:w-20"
                      :class="{
                        'bg-yellow-300 text-black shadow-lg shadow-yellow-400/30': isMember(user),
                        'bg-white/15 text-white': !isMember(user),
                        'opacity-60 cursor-not-allowed': isAdmin(user) || userProcessingId === user.id
                      }"
                      :disabled="isAdmin(user) || userProcessingId === user.id"
                      @click="handleToggleUser(user)"
                    >
                      <span
                        class="absolute left-0.5 top-0.5 h-7 w-7 rounded-full bg-white transition-all duration-200 sm:left-1 sm:top-1 sm:h-8 sm:w-8"
                        :class="isMember(user) ? 'translate-x-8 bg-yellow-600/80 shadow-lg shadow-yellow-500/50 sm:translate-x-9' : 'translate-x-0 bg-white/90 shadow-lg shadow-black/20'"
                      ></span>
                      <span class="w-full text-center text-[10px] font-bold tracking-wide sm:text-xs">
                        {{ isMember(user) ? "ON" : "OFF" }}
                      </span>
                    </button>
                    <button
                      v-if="canBeVerified(user)"
                      class="relative flex h-9 w-[88px] items-center rounded-full border border-white/10 transition-all duration-200 sm:h-10 sm:w-24"
                      :class="{
                        'bg-green-400/80 text-black shadow-lg shadow-green-400/40': isVerified(user),
                        'bg-white/15 text-white': !isVerified(user),
                        'opacity-60 cursor-not-allowed': userVerifyProcessingId === user.id
                      }"
                      :disabled="userVerifyProcessingId === user.id"
                      @click="handleToggleVerify(user)"
                    >
                      <span
                        class="absolute left-0.5 top-0.5 h-7 w-7 rounded-full bg-white transition-all duration-200 sm:left-1 sm:top-1 sm:h-8 sm:w-8"
                        :class="isVerified(user) ? 'translate-x-[58px] bg-green-600/80 shadow-lg shadow-green-500/50 sm:translate-x-[68px]' : 'translate-x-0 bg-white/90 shadow-lg shadow-black/20'"
                      ></span>
                      <span class="w-full text-center text-[10px] font-bold tracking-wide sm:text-xs">
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
            <p v-if="sortedNews.length === 0" class="rounded-xl border border-white/10 bg-white/5 px-4 py-8 text-center text-xs text-gray-200 sm:rounded-2xl sm:px-6 sm:py-10 sm:text-sm">
              No news articles available right now. Encourage members to submit new stories.
            </p>
            <ul v-else class="space-y-3 sm:space-y-4">
              <li
                v-for="item in sortedNews"
                :key="item.id"
                class="relative flex flex-col gap-3 rounded-2xl border border-white/10 bg-white/10 p-4 transition hover:border-orange-300/40 hover:bg-white/15 sm:gap-4 sm:rounded-3xl sm:flex-row sm:items-center sm:justify-between sm:p-6"
              >
                <div class="space-y-2 flex-1 min-w-0 sm:space-y-3">
                  <div class="flex flex-col gap-1.5 sm:gap-2">
                    <h3 class="text-lg font-semibold text-white sm:text-xl break-words">
                      {{ item.title }}
                    </h3>
                    <p class="max-w-2xl text-xs text-gray-200 sm:text-sm lg:text-base break-words">
                      {{ item.summary ?? truncate(item.content, 180) }}
                    </p>
                  </div>
                  <div class="flex flex-wrap gap-1.5 sm:gap-3 text-[10px] uppercase tracking-wide text-gray-300/90 sm:text-xs">
                    <span class="flex items-center gap-1.5 rounded-full bg-white/10 px-2 py-0.5 sm:px-3 sm:py-1">
                      {{ item.reporter }}
                      <VerifiedBadge :verified="item.reporterVerified" size="xs" />
                    </span>
                    <span class="rounded-full bg-white/10 px-2 py-0.5 sm:px-3 sm:py-1">
                      {{ formatDate(item.reportedAt) }}
                    </span>
                    <span class="rounded-full bg-orange-400/20 px-2 py-0.5 text-orange-100 sm:px-3 sm:py-1">
                      {{ item.status ?? "UNVERIFIED" }}
                    </span>
                  </div>
                </div>
                <div class="flex flex-row items-center justify-between gap-3 sm:flex-col sm:items-end sm:gap-4">
                  <span
                    class="rounded-full px-3 py-1 text-xs font-semibold tracking-wide sm:px-4 sm:text-sm whitespace-nowrap"
                    :class="isVisible(item) ? 'bg-green-400/20 text-green-200' : 'bg-red-500/20 text-red-200'"
                  >
                    {{ isVisible(item) ? "Visible" : "Invisible" }}
                  </span>
                  <button
                    class="relative flex h-9 w-20 items-center justify-center gap-1.5 rounded-full border border-white/10 text-xs font-semibold uppercase tracking-wide transition-all duration-200 sm:h-10 sm:w-24 sm:gap-2 sm:text-sm"
                    :class="{
                      'bg-green-400/80 text-black shadow-lg shadow-green-400/40': isVisible(item),
                      'bg-white/15 text-white': !isVisible(item),
                      'opacity-60 cursor-not-allowed': newsProcessingId === item.id
                    }"
                    :disabled="newsProcessingId === item.id"
                    @click="handleToggleNews(item)"
                  >
                    <span class="relative h-4 w-6 sm:h-5 sm:w-8">
                      <svg
                        v-if="isVisible(item)"
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        class="h-4 w-4 fill-current sm:h-5 sm:w-5"
                      >
                        <path
                          d="M12 5c-6.5 0-9.27 6.27-9.38 6.52a1 1 0 0 0 0 .96C2.73 12.73 5.5 19 12 19s9.27-6.27 9.38-6.52a1 1 0 0 0 0-.96C21.27 11.27 18.5 5 12 5Zm0 12c-4.65 0-6.97-4.21-7.35-5 .38-.79 2.7-5 7.35-5s6.97 4.21 7.35 5c-.38.79-2.7 5-7.35 5Zm0-8a3 3 0 1 0 3 3 3 3 0 0 0-3-3Zm0 4a1 1 0 1 1 1-1 1 1 0 0 1-1 1Z"
                        />
                      </svg>
                      <svg
                        v-else
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        class="h-4 w-4 fill-current sm:h-5 sm:w-5"
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
            <p v-if="sortedComments.length === 0" class="rounded-xl border border-white/10 bg-white/5 px-4 py-8 text-center text-xs text-gray-200 sm:rounded-2xl sm:px-6 sm:py-10 sm:text-sm">
              No comments available yet. Encourage members to share their thoughts.
            </p>
            <ul v-else class="space-y-3 sm:space-y-4">
              <li
                v-for="comment in sortedComments"
                :key="comment.id"
                class="relative flex flex-col gap-3 rounded-2xl border border-white/10 bg-white/10 p-4 transition hover:border-orange-300/40 hover:bg-white/15 sm:gap-4 sm:rounded-3xl sm:flex-row sm:items-center sm:justify-between sm:p-6"
              >
                <div class="space-y-2 flex-1 min-w-0 sm:space-y-3">
                  <div class="flex flex-col gap-1.5 sm:gap-2">
                    <h3 class="text-base font-semibold text-white sm:text-lg break-words">
                      {{ comment.newsTitle ?? 'Untitled news' }}
                    </h3>
                    <p class="text-xs text-gray-200/90 sm:text-sm break-words">
                      {{ comment.text || '— No comment text provided —' }}
                    </p>
                  </div>
                  <div class="flex flex-wrap gap-1.5 sm:gap-3 text-[10px] uppercase tracking-wide text-gray-300/90 sm:text-xs">
                    <span class="flex items-center gap-1.5 rounded-full bg-white/10 px-2 py-0.5 sm:px-3 sm:py-1">
                      {{ comment.username }}
                      <VerifiedBadge :verified="comment.userVerified" size="xs" />
                    </span>
                    <span class="rounded-full bg-white/10 px-2 py-0.5 sm:px-3 sm:py-1">
                      {{ formatDateTime(comment.createdAt) }}
                    </span>
                    <span
                      class="rounded-full px-2 py-0.5 sm:px-3 sm:py-1"
                      :class="comment.voteType === 'FAKE'
                        ? 'bg-red-500/20 text-red-200'
                        : 'bg-green-500/20 text-green-200'"
                    >
                      {{ comment.voteType }}
                    </span>
                  </div>
                </div>
                <div class="flex flex-row items-center justify-between gap-3 sm:flex-col sm:items-end sm:gap-4">
                  <span
                    class="rounded-full px-3 py-1 text-xs font-semibold tracking-wide sm:px-4 sm:text-sm whitespace-nowrap"
                    :class="isCommentVisible(comment) ? 'bg-green-400/20 text-green-200' : 'bg-red-500/20 text-red-200'"
                  >
                    {{ isCommentVisible(comment) ? "Visible" : "Hidden" }}
                  </span>
                  <button
                    class="relative flex h-9 w-20 items-center justify-center gap-1.5 rounded-full border border-white/10 text-xs font-semibold uppercase tracking-wide transition-all duration-200 sm:h-10 sm:w-24 sm:gap-2 sm:text-sm"
                    :class="{
                      'bg-green-400/80 text-black shadow-lg shadow-green-400/40': isCommentVisible(comment),
                      'bg-white/15 text-white': !isCommentVisible(comment),
                      'opacity-60 cursor-not-allowed': commentProcessingId === comment.id
                    }"
                    :disabled="commentProcessingId === comment.id"
                    @click="handleToggleComment(comment)"
                  >
                    <span class="relative h-4 w-6 sm:h-5 sm:w-8">
                      <svg
                        v-if="isCommentVisible(comment)"
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        class="h-4 w-4 fill-current sm:h-5 sm:w-5"
                      >
                        <path
                          d="M5 5a1 1 0 0 0-.92.62l-3 7a1 1 0 0 0 .08.9l3 5A1 1 0 0 0 5 19h14a1 1 0 0 0 .86-.5l3-5a1 1 0 0 0 .08-.9l-3-7A1 1 0 0 0 19 5Zm1.38 2h11.24l2.14 5-2.14 3.5H6.38L4.24 12Z"
                        />
                      </svg>
                      <svg
                        v-else
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        class="h-4 w-4 fill-current sm:h-5 sm:w-5"
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
        class="rounded-xl border border-red-400/50 bg-red-900/30 px-3 py-2 text-xs text-red-100 shadow-lg shadow-red-900/30 sm:rounded-2xl sm:px-4 sm:py-3 sm:text-sm"
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
  if (!user.roles) {
    return false;
  }
  for (const role of user.roles) {
    const roleName = role?.toString();
    if (roleName === "MEMBER" || roleName === "ROLE_MEMBER") {
      return true;
    }
  }
  return false;
}

function isAdmin(user: User): boolean {
  if (!user.roles) {
    return false;
  }
  for (const role of user.roles) {
    const roleName = role?.toString();
    if (roleName === "ADMIN" || roleName === "ROLE_ADMIN") {
      return true;
    }
  }
  return false;
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
    const currentlyVisible = isVisible(news);
    // If we mark it invisible, only admins will see it; otherwise everyone can see it.
    if (currentlyVisible) {
      await store.setNewsVisibility(news.id, false);
    } else {
      await store.setNewsVisibility(news.id, true);
    }
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
    const currentlyVisible = isCommentVisible(comment);
    // Invisible comments remain for admins only; visible comments appear for all readers.
    let updated: Comment;
    if (currentlyVisible) {
      updated = await store.setCommentVisibility(comment.id, false);
    } else {
      updated = await store.setCommentVisibility(comment.id, true);
    }
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
