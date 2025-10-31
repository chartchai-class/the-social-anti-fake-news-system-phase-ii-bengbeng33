<template>
  <div >
    <div class="container mx-auto px-4 py-8">
      <div class="max-w-4xl mx-auto">
        <!-- Profile Card -->
        <div class="bg-white/20 -md rounded-2xl shadow-2xl p-6 md:p-8 border border-white/20">
          <h1 class="text-4xl md:text-5xl font-bold text-white text-center mb-8">My Profile</h1>
          
          <div class="grid grid-cols-1 md:grid-cols-3 gap-6 md:gap-8">
            <!-- Left Section: Profile Picture and Role -->
            <div class="flex flex-col items-center space-y-4">
              <!-- Profile Picture -->
              <div class="relative">
                <div class="w-32 h-32 md:w-40 md:h-40 rounded-full bg-gray-800 border-4 border-white/30 flex items-center justify-center overflow-hidden">
                  <img
                    v-if="profile?.profileImagePath"
                    :src="profile.profileImagePath"
                    :alt="profileName"
                    class="w-full h-full object-cover"
                  />
                  <svg
                    v-else
                    class="w-16 h-16 md:w-20 md:h-20 text-white/50"
                    fill="currentColor"
                    viewBox="0 0 20 20"
                  >
                    <path
                      fill-rule="evenodd"
                      d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z"
                      clip-rule="evenodd"
                    />
                  </svg>
                </div>
              </div>
              
              <!-- My Role -->
              <div class="text-center">
                <p class="text-white/80 text-sm font-medium mb-2">My Role</p>
                <div class="inline-block px-4 py-2 bg-white/20 backdrop-blur-sm rounded-lg border border-white/30">
                  <span class="text-white font-semibold">{{ displayRole }}</span>
                </div>
              </div>
            </div>

            <!-- Right Section: User Info -->
            <div class="md:col-span-2 space-y-4">
              <!-- Username -->
              <div>
                <label class="block text-white/80 text-sm font-medium mb-2">Username</label>
                <div class="flex items-center gap-2">
                  <input
                    type="text"
                    :value="profileName"
                    readonly
                    class="flex-1 px-4 py-2 bg-white/20 backdrop-blur-sm rounded-lg border border-white/30 text-white placeholder-white/50 focus:outline-none focus:ring-2 focus:ring-white/50"
                  />
                  <VerifiedBadge :verified="profile?.verified" size="md" />
                </div>
              </div>

              <!-- Email -->
              <div>
                <label class="block text-white/80 text-sm font-medium mb-2">E-mail</label>
                <input
                  type="email"
                  :value="profile?.email || ''"
                  readonly
                  class="w-full px-4 py-2 bg-white/20 backdrop-blur-sm rounded-lg border border-white/30 text-white placeholder-white/50 focus:outline-none focus:ring-2 focus:ring-white/50"
                />
              </div>

              <!-- News Reported -->
              <div>
                <label class="block text-white/80 text-sm font-medium mb-2">News Reported :</label>
                <div class="px-4 py-2 bg-white/20 backdrop-blur-sm rounded-lg border border-white/30 text-white">
                  {{ profile?.newsReportedCount ?? 0 }}
                </div>
              </div>

              <!-- Commented -->
              <div>
                <label class="block text-white/80 text-sm font-medium mb-2">Commented :</label>
                <div class="px-4 py-2 bg-white/20 backdrop-blur-sm rounded-lg border border-white/30 text-white">
                  {{ profile?.commentedCount ?? 0 }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import UserService from "@/services/UserService";
import type { UserProfile } from "@/types";
import VerifiedBadge from "@/components/VerifiedBadge.vue";

const router = useRouter();
const profile = ref<UserProfile | null>(null);
const loading = ref(true);
const error = ref<string | null>(null);

const profileName = computed(() => {
  if (!profile.value) return "";
  return `${profile.value.name} ${profile.value.surname}`.trim() || profile.value.username;
});

const displayRole = computed(() => {
  if (!profile.value?.roles || profile.value.roles.length === 0) return "READER";
  const roles = profile.value.roles;
  if (roles.includes("ADMIN")) return "ADMIN";
  if (roles.includes("MEMBER")) return "MEMBER";
  return "READER";
});

async function fetchProfile() {
  try {
    loading.value = true;
    error.value = null;
    profile.value = await UserService.getMyProfile();
  } catch (err: any) {
    console.error("Error fetching profile:", err);
    error.value = "Failed to load profile. Please try again.";
    if (err.response?.status === 401) {
      router.push("/login");
    }
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  fetchProfile();
});
</script>

<style scoped>
/* Additional styles if needed */
</style>
