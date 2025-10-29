<template>
  <div class="min-h-screen relative overflow-hidden bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900">
    <!-- Background Effects -->
    <div class="absolute inset-0 overflow-hidden">
      <div class="absolute top-0 right-0 w-96 h-96 bg-red-500/20 rounded-full blur-3xl"></div>
      <div class="absolute bottom-0 left-0 w-96 h-96 bg-orange-500/20 rounded-full blur-3xl"></div>
      <div class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-gray-800/40 rounded-full blur-3xl"></div>
    </div>

    <!-- Register Form Container -->
    <div class="relative z-10 flex items-center justify-center min-h-screen py-12 px-4 sm:px-6 lg:px-8">
      <div class="w-full max-w-4xl">
        <!-- Frosted Glass Form -->
        <div class="bg-white/80 backdrop-blur-md rounded-2xl shadow-2xl border border-gray-200/50 p-8 sm:p-12">
          <h1 class="text-4xl sm:text-5xl font-bold text-gray-900 mb-8">Register</h1>
          
          <form @submit.prevent="handleRegister" class="space-y-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- Name -->
              <div>
                <input
                  v-model="form.name"
                  type="text"
                  required
                  placeholder="Name"
                  class="w-full px-4 py-3 bg-gray-100 rounded-lg border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all"
                />
              </div>
  
              <!-- Surname -->
              <div>
                <input
                  v-model="form.surname"
                  type="text"
                  required
                  placeholder="Surname"
                  class="w-full px-4 py-3 bg-gray-100 rounded-lg border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all"
                />
              </div>
            </div>
  
            <div class="flex gap-6">
              <!-- Email -->
              <div class="flex-1">
                <input
                  v-model="form.email"
                  type="email"
                  required
                  placeholder="Email Address"
                  class="w-full px-4 py-3 bg-gray-100 rounded-lg border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all"
                />
              </div>

              <!-- Profile Picture Upload -->
              <div class="flex items-start justify-center ml-4">
                <div class="relative">
                  <label
                    for="profile-upload"
                    class="cursor-pointer flex flex-col items-center justify-center w-32 h-32 bg-gray-100 rounded-full border-2 border-black hover:bg-gray-200 transition-colors"
                  >
                    <input
                      id="profile-upload"
                      type="file"
                      accept="image/*"
                      @change="handleProfileUpload"
                      class="hidden"
                    />
                    <svg
                      v-if="!profilePreview"
                      class="w-12 h-12 text-gray-600 mb-1"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                      />
                    </svg>
                    <img
                      v-else
                      :src="profilePreview"
                      alt="Profile preview"
                      class="w-28 h-28 rounded-full object-cover"
                    />
                    <svg
                      class="w-5 h-5 text-gray-900 mt-1"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M7 11l5-5m0 0l5 5m-5-5v12"
                      />
                    </svg>
                  </label>
                </div>
              </div>
            </div>
  
            <!-- Password -->
            <div>
              <input
                v-model="form.password"
                type="password"
                required
                placeholder="Password"
                class="w-full px-4 py-3 bg-gray-100 rounded-lg border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all"
              />
            </div>
  
            <!-- Confirm Password -->
            <div>
              <input
                v-model="form.confirmPassword"
                type="password"
                required
                placeholder="Confirm Password"
                class="w-full px-4 py-3 bg-gray-100 rounded-lg border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all"
              />
              <p v-if="passwordMismatch" class="mt-2 text-sm text-red-500">
                Passwords do not match
              </p>
            </div>
  
            <!-- Error Message -->
            <div v-if="errorMessage" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg">
              {{ errorMessage }}
            </div>
  
            <!-- Sign Up Button and Login Link -->
            <div class="flex items-center justify-between pt-4">
              <button
                type="submit"
                :disabled="isSubmitting || passwordMismatch"
                class="px-8 py-3 bg-gray-300 text-gray-900 rounded-lg font-medium hover:bg-gray-400 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <span v-if="!isSubmitting">Sign Up</span>
                <span v-else>Signing Up...</span>
              </button>

              <!-- Login Link -->
              <div class="text-gray-900">
                Already have an account?
                <router-link
                  to="/login"
                  class="font-bold text-gray-900 hover:text-orange-500 transition-colors underline"
                >
                  Log In
                </router-link>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
  
  <script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const form = ref({
  name: '',
  surname: '',
  email: '',
  password: '',
  confirmPassword: '',
});

const profileFile = ref<File | null>(null);
const profilePreview = ref<string | null>(null);
const isSubmitting = ref(false);
const errorMessage = ref('');

const passwordMismatch = computed(() => {
  return (
    form.value.confirmPassword !== '' &&
    form.value.password !== form.value.confirmPassword
  );
});

function handleProfileUpload(event: Event) {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (file) {
    profileFile.value = file;
    const reader = new FileReader();
    reader.onload = (e) => {
      profilePreview.value = e.target?.result as string;
    };
    reader.readAsDataURL(file);
  }
}

async function handleRegister() {
  if (passwordMismatch.value) {
    errorMessage.value = 'Passwords do not match';
      return;
    }
  
  isSubmitting.value = true;
  errorMessage.value = '';

  try {
    // TODO: Implement actual registration API call
    // For now, just simulate a delay and redirect
    await new Promise((resolve) => setTimeout(resolve, 1000));
    
    // Store user info (you can replace this with actual auth logic)
    localStorage.setItem('user', JSON.stringify({
      name: form.value.name,
      surname: form.value.surname,
      email: form.value.email,
    }));

    // Redirect to home page
    router.push('/');
  } catch {
    errorMessage.value = 'Registration failed. Please try again.';
  } finally {
    isSubmitting.value = false;
  }
  }
  </script>

<style scoped>
/* Additional styles if needed */
</style>
  