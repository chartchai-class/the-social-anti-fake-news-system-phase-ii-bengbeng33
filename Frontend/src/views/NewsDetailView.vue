<template>
  <div class="min-h-screen">
    <div v-if="loading" class="fixed top-0 left-0 right-0 z-50">
      <div class="h-1 bg-blue-200">
        <div
          class="h-full bg-blue-500 transition-all duration-300 ease-out"
          :style="{ width: loadingProgress + '%' }"
        ></div>
      </div>
    </div>

    <div class="flex justify-center">
      <div class="relative inline-block -mt-2 mb-6">
        <img
          src="/H.png"
          alt="Daybreak banner"
          class="w-[520px] max-w-full h-auto pointer-events-none select-none"
        />
        <h1 class="absolute inset-0 grid place-items-center z-10 pointer-events-none">
          <span
            class="px-4 py-1 rounded-xl text-black text-3xl sm:text-4xl font-bold tracking-wide backdrop-blur-[1px]"
          >
            News Detail
          </span>
        </h1>
      </div>
    </div>

    <div v-if="!newsItem" class="text-white text-center py-20">
      <p class="text-xl">News not found</p>
      <router-link
        to="/"
        class="text-orange-500 hover:text-orange-300 mt-4 inline-block"
      >
        ← Back to All News
      </router-link>
    </div>

    <div
      v-else
      class="px-4 sm:px-6 lg:px-8 py-6 max-w-6xl mx-auto space-y-8"
    >
      <section class="bg-white/95 rounded-2xl p-6 shadow-md">
        <div class="flex flex-col gap-8 lg:grid lg:grid-cols-4">
          <div class="lg:col-span-3 space-y-4">
            <h1 class="text-2xl font-bold text-gray-900">
              {{ newsItem.title }}
            </h1>
            <div
              class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 text-sm text-gray-600"
            >
              <div>
                by <span class="font-medium">{{ newsItem.reporter }}</span>
                <span class="mx-1">·</span>
                <time :datetime="newsItem.reportedAt">
                  {{ formatDate(newsItem.reportedAt) }}
                </time>
              </div>
              <button
                @click="openModal"
                class="px-4 py-2 bg-orange-600 text-white rounded-md hover:bg-orange-700 transition-colors"
              >
                Vote & Comment
              </button>
            </div>

            <img
              v-if="newsItem.imageUrl"
              :src="newsItem.imageUrl"
              :alt="newsItem.title"
              class="w-full max-h-[440px] object-cover rounded-xl"
            />

            <p v-if="newsItem.summary" class="text-gray-700 leading-relaxed">
              {{ newsItem.summary }}
            </p>

            <p class="text-gray-800 leading-relaxed whitespace-pre-line">
              {{ newsItem.content }}
            </p>
          </div>

          <aside
            class="lg:col-span-1 bg-gray-50 rounded-xl p-5 flex flex-col items-center space-y-4 border border-gray-200"
          >
            <h2 class="text-xl font-semibold text-gray-900">Current Status</h2>
            <img
              :src="currentStatusImage"
              :alt="currentStatusText"
              class="w-36 h-36 object-contain"
            />
            <div class="w-full text-center space-y-2">
              <div class="text-sm text-gray-600 uppercase tracking-wide">
                Fact Votes
              </div>
              <div class="text-2xl font-bold text-green-600">
                {{ voteCounts.notFake }}
              </div>
              <div class="text-sm text-gray-600 uppercase tracking-wide pt-3">
                Fake Votes
              </div>
              <div class="text-2xl font-bold text-red-600">
                {{ voteCounts.fake }}
              </div>
            </div>
            <p v-if="hasVoted" class="text-xs text-gray-500 text-center">
              You have already voted on this article.
            </p>
          </aside>
        </div>
      </section>

      <section class="bg-white/95 rounded-2xl p-6 shadow-md">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-xl font-bold text-gray-900">
            Comments
            <span class="text-base text-gray-500">({{ comments.length }})</span>
          </h3>
          <button
            @click="openModal"
            class="px-4 py-2 bg-orange-600 text-white rounded-md hover:bg-orange-700 transition-colors"
          >
            Vote & Comment
          </button>
        </div>

        <div v-if="loadingComments" class="py-12 text-center text-gray-500">
          Loading comments...
        </div>
        <div v-else-if="comments.length === 0" class="py-12 text-center text-gray-500">
          No comments yet. Be the first to share your thoughts!
        </div>
        <div v-else class="space-y-4">
          <article
            v-for="comment in comments"
            :key="comment.id"
            :class="commentCardClass(comment.voteType)"
            class="rounded-xl p-4 border transition-shadow"
          >
            <div class="flex items-start justify-between gap-4">
              <div>
                <div class="text-lg font-semibold text-gray-900">
                  {{ comment.username }}
                </div>
                <div
                  :class="voteBadgeClass(comment.voteType)"
                  class="inline-flex items-center px-2 py-0.5 mt-1 text-xs font-bold uppercase rounded-full"
                >
                  {{ comment.voteType === "FACT" ? "Fact" : "Fake" }}
                </div>
              </div>
              <div class="text-sm text-gray-600">
                {{ formatDate(comment.createdAt) }}
              </div>
            </div>

            <p class="mt-3 text-gray-800 whitespace-pre-line">
              {{ comment.text }}
            </p>

            <img
              v-if="comment.imageUrl"
              :src="comment.imageUrl"
              :alt="`Image shared by ${comment.username}`"
              class="mt-3 w-full max-h-72 object-contain rounded-lg bg-white"
            />
          </article>
        </div>
      </section>

      <div class="mt-4 text-center">
        <router-link
          to="/"
          class="inline-flex items-center px-6 py-3 bg-orange-600 text-white rounded-lg hover:bg-orange-700 transition-colors"
        >
          Back
        </router-link>
      </div>
    </div>

    <div
      v-if="showModal"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm px-4"
    >
      <div class="bg-white rounded-2xl w-full max-w-xl shadow-2xl p-6 relative">
        <button
          class="absolute top-3 right-3 text-gray-400 hover:text-gray-600"
          @click="closeModal"
          aria-label="Close dialog"
        >
          <span class="text-2xl leading-none">&times;</span>
        </button>

        <h3 class="text-xl font-semibold text-gray-900 mb-4">
          Share your thoughts!
        </h3>
        <p v-if="hasVoted" class="text-sm text-gray-500 mb-4">
          Your existing vote will remain. Updating your comment will not change
          the overall tally.
        </p>

        <form @submit.prevent="submitComment" class="space-y-4">
          <div class="text-sm text-gray-600">
            Posting as
            <span class="font-semibold text-gray-800">
              {{ currentUser?.username ?? "your account" }}
            </span>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >Comment</label
            >
            <textarea
              v-model="newComment.text"
              rows="4"
              placeholder="Write your comment..."
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
              required
            ></textarea>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >Image URL (optional)</label
            >
            <input
              v-model="newComment.imageUrl"
              type="url"
              placeholder="https://example.com/image.jpg"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
            />
          </div>

          <div>
            <p class="text-sm font-medium text-gray-700 mb-2">
              How do you rate this news?
            </p>
            <div class="grid grid-cols-2 gap-4">
              <button
                type="button"
                :class="voteOptionClass('FACT')"
                @click="selectVote('FACT')"
              >
                <img
                  src="/Fact.png"
                  alt="Fact"
                  class="w-24 h-24 mx-auto object-contain"
                />
                <div class="mt-2 text-sm font-semibold">
                  Fact ({{ voteCounts.notFake }})
                </div>
              </button>
              <button
                type="button"
                :class="voteOptionClass('FAKE')"
                @click="selectVote('FAKE')"
              >
                <img
                  src="/Fake.png"
                  alt="Fake"
                  class="w-24 h-24 mx-auto object-contain"
                />
                <div class="mt-2 text-sm font-semibold">
                  Fake ({{ voteCounts.fake }})
                </div>
              </button>
            </div>
          </div>

          <p v-if="modalError" class="text-sm text-red-600">
            {{ modalError }}
          </p>

          <div class="flex justify-end gap-3 pt-2">
            <button
              type="button"
              class="px-4 py-2 text-gray-600 hover:text-gray-800"
              @click="closeModal"
            >
              Cancel
            </button>
            <button
              type="submit"
              :disabled="isSubmitting"
              class="px-5 py-2 bg-orange-600 text-white rounded-md hover:bg-orange-700 transition-colors disabled:opacity-60 disabled:cursor-not-allowed"
            >
              {{ isSubmitting ? "Submitting..." : "Submit" }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, reactive, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useNewsStore } from "@/stores/news";
import type { Comment, Status, User } from "@/types";

type VoteChoice = "FAKE" | "FACT";

interface Props {
  id: string;
}

const props = defineProps<Props>();
const route = useRoute();
const router = useRouter();
const newsStore = useNewsStore();
newsStore.initializeAuthSync();

const loading = ref(false);
const loadingProgress = ref(0);
const comments = ref<Comment[]>([]);
const loadingComments = ref(false);
const voteCounts = ref({ fake: 0, notFake: 0 });
const hasVoted = ref(false);
const selectedVote = ref<VoteChoice | null>(null);
const showModal = ref(false);
const modalError = ref("");
const isSubmitting = ref(false);
const currentUser = ref<User | null>(null);
const newComment = reactive({
  text: "",
  imageUrl: "",
});

function loadCurrentUser() {
  const raw = localStorage.getItem("user");
  if (!raw) {
    currentUser.value = null;
    return;
  }
  try {
    currentUser.value = JSON.parse(raw) as User;
  } catch (error) {
    console.warn("Unable to parse stored user", error);
    currentUser.value = null;
  }
}

function simulateLoading() {
  loading.value = true;
  loadingProgress.value = 0;

  const interval = setInterval(() => {
    loadingProgress.value += Math.random() * 35;
    if (loadingProgress.value >= 100) {
      loadingProgress.value = 100;
      setTimeout(() => {
        loading.value = false;
        loadingProgress.value = 0;
      }, 200);
      clearInterval(interval);
    }
  }, 70);
}

const newsId = computed(() => props.id || (route.params.id as string));
const numericNewsId = computed(() => Number.parseInt(newsId.value, 10));

const newsItem = computed(() => newsStore.getNewsById(newsId.value));
const currentStatus = computed((): Status => newsStore.getCurrentStatus(newsId.value));

function getVoteChoiceKey(): string | null {
  const userId = currentUser.value?.id;
  const newsValue = numericNewsId.value;
  if (!userId || Number.isNaN(newsValue)) {
    return null;
  }
  return `voteChoice:${userId}:${newsValue}`;
}

function getStoredVoteChoice(): VoteChoice | null {
  const key = getVoteChoiceKey();
  if (!key) {
    return null;
  }
  const stored = localStorage.getItem(key);
  return stored === "FAKE" || stored === "FACT" ? stored : null;
}

function resolveVoteFromComments(): VoteChoice | null {
  const userId = currentUser.value?.id;
  if (!userId) {
    return null;
  }
  const entry = comments.value.find((comment) => comment.userId === userId);
  return entry ? entry.voteType : null;
}

function syncSelectedVote() {
  const stored = getStoredVoteChoice();
  if (stored) {
    selectedVote.value = stored;
    return;
  }
  const fromComments = resolveVoteFromComments();
  if (fromComments) {
    selectedVote.value = fromComments;
    return;
  }
  if (!hasVoted.value) {
    selectedVote.value = null;
  }
}

const currentStatusText = computed(() => {
  switch (currentStatus.value) {
    case "FAKE":
      return "FAKE";
    case "FACT":
      return "FACT";
    default:
      return "UNVERIFIED";
  }
});

const currentStatusImage = computed(() => {
  switch (currentStatus.value) {
    case "FAKE":
      return "/Fake.png";
    case "FACT":
      return "/Fact.png";
    default:
      return "/Equal.png";
  }
});

async function updateUserVoteStatus() {
  if (Number.isNaN(numericNewsId.value) || !currentUser.value) {
    hasVoted.value = false;
    selectedVote.value = null;
    return;
  }
  const voted = await newsStore.refreshUserVoteStatus(numericNewsId.value);
  hasVoted.value = voted;
  syncSelectedVote();
}

async function fetchVoteCounts() {
  if (Number.isNaN(numericNewsId.value)) {
    voteCounts.value = { fake: 0, notFake: 0 };
    return;
  }
  voteCounts.value = await newsStore.getVoteCounts(numericNewsId.value);
}

async function fetchComments() {
  if (Number.isNaN(numericNewsId.value)) {
    comments.value = [];
    return;
  }
  loadingComments.value = true;
  try {
    comments.value = await newsStore.getComments(numericNewsId.value);
  } finally {
    loadingComments.value = false;
  }
}

async function loadNewsDetail() {
  simulateLoading();
  if (!Number.isNaN(numericNewsId.value)) {
    await newsStore.fetchNewsById(numericNewsId.value);
  }
  await Promise.all([fetchVoteCounts(), fetchComments(), updateUserVoteStatus()]);
  syncSelectedVote();
}

onMounted(() => {
  loadCurrentUser();
  loadNewsDetail();
  globalThis.addEventListener("storage", loadCurrentUser);
  globalThis.addEventListener("auth-changed", loadCurrentUser);
});

onUnmounted(() => {
  globalThis.removeEventListener("storage", loadCurrentUser);
  globalThis.removeEventListener("auth-changed", loadCurrentUser);
});

watch(newsId, async () => {
  await loadNewsDetail();
});

watch(currentUser, async () => {
  await updateUserVoteStatus();
  syncSelectedVote();
});

const commentCardClass = (voteType: VoteChoice) =>
  voteType === "FACT"
    ? "bg-green-50 border-green-300/70 hover:shadow-green-100"
    : "bg-red-50 border-red-300/70 hover:shadow-red-100";

const voteBadgeClass = (voteType: VoteChoice) =>
  voteType === "FACT" ? "bg-green-600 text-white" : "bg-red-600 text-white";

const voteOptionClass = (choice: VoteChoice) => {
  const classes = [
    "border",
    "rounded-xl",
    "p-4",
    "text-center",
    "transition",
    "bg-white",
    "hover:shadow-lg",
  ];
  if (selectedVote.value === choice) {
    classes.push(
      choice === "FACT"
        ? "border-green-500 shadow-green-200/60 shadow-xl"
        : "border-red-500 shadow-red-200/60 shadow-xl"
    );
  } else {
    classes.push("border-gray-200");
  }
  return classes.join(" ");
};

const openModal = async () => {
  if (!currentUser.value) {
    await router.push({
      path: "/login",
      query: { redirect: route.fullPath },
    });
    return;
  }
  modalError.value = "";
  await updateUserVoteStatus();
  syncSelectedVote();
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  modalError.value = "";
  isSubmitting.value = false;
};

const selectVote = (choice: VoteChoice) => {
  selectedVote.value = choice;
};

async function submitComment() {
  modalError.value = "";

  if (!newComment.text.trim()) {
    modalError.value = "Please enter your comment.";
    return;
  }

  const effectiveVote = selectedVote.value;
  if (!effectiveVote) {
    modalError.value = "Please choose Fact or Fake.";
    return;
  }

  if (hasVoted.value) {
    modalError.value = "You have already shared your thoughts on this article.";
    return;
  }

  isSubmitting.value = true;
  try {
    await newsStore.addComment(numericNewsId.value, {
      text: newComment.text,
      imageUrl: newComment.imageUrl || undefined,
      voteType: effectiveVote,
    });

    await newsStore.refreshUserVoteStatus(numericNewsId.value);
    hasVoted.value = true;
    selectedVote.value = effectiveVote;
    await fetchVoteCounts();
    comments.value = await newsStore.getComments(numericNewsId.value);
    syncSelectedVote();
    newComment.text = "";
    newComment.imageUrl = "";
    modalError.value = "";
    closeModal();
  } catch (error) {
    console.error("Failed to submit comment", error);
    modalError.value = "Unable to submit your comment right now.";
  } finally {
    isSubmitting.value = false;
  }
}

function formatDate(iso: string) {
  if (!iso) return "";
  const d = new Date(iso);
  if (Number.isNaN(d.getTime())) return iso;
  return d.toLocaleString("en-US", {
    dateStyle: "medium",
    timeStyle: "short",
  });
}
</script>
