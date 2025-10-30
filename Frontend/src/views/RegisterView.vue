<template>
  <div>
    <!-- Background Effects -->
    <div class="absolute inset-0 overflow-hidden">
      <!-- Reddish-orange glow at top right -->
      <div class="absolute top-0 right-0 w-96 h-96 bg-orange-500/30 rounded-full blur-3xl"></div>
      <!-- Purplish-pink glow at bottom left -->
      <div class="absolute bottom-0 left-0 w-96 h-96 bg-pink-500/30 rounded-full blur-3xl"></div>
    </div>

    <!-- Register Form Container -->
    <div class="relative z-10 flex items-center justify-center min-h-screen py-12 px-4 sm:px-6 lg:px-8">
      <div class="w-full max-w-4xl mx-auto">
        <!-- Frosted Glass Form (Glassmorphism) -->
        <div class="bg-gray-100/20 backdrop-blur-md rounded-3xl shadow-2xl border border-gray-300/50 p-8 sm:p-12">
          <h1 class="text-4xl sm:text-5xl font-bold text-black mb-8 
          drop-shadow-[2px_2px_3px_rgba(255,255,255,0.8)] text-center">
            Register
          </h1>

          <form @submit.prevent="handleRegister" class="space-y-6">
            <!-- Two Column Layout -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
              <!-- Column 1: Input Fields (Left - 2/3) -->
              <div class="md:col-span-2 space-y-6">
                <!-- Name and Surname Row -->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <!-- Name -->
                  <div>
                    <input v-model="form.name" type="text" required placeholder="Name"
                      class="w-full h-[60px] px-4 py-3 bg-gray-100 rounded-full border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all" />
                  </div>

                  <!-- Surname -->
                  <div>
                    <input v-model="form.surname" type="text" required placeholder="Surname"
                      class="w-full h-[60px] px-4 py-3 bg-gray-100 rounded-full border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all" />
                  </div>
                </div>

                <!-- Email -->
                <div>
                  <input v-model="form.email" type="email" required placeholder="Email Address"
                    class="w-full h-[60px] px-4 py-3 bg-gray-100 rounded-full border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all" />
                </div>

                <!-- Password -->
                <div>
                  <input v-model="form.password" type="password" required placeholder="Password"
                    class="w-full h-[60px] px-4 py-3 bg-gray-100 rounded-full border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all" />
                </div>

                <!-- Confirm Password -->
                <div>
                  <input v-model="form.confirmPassword" type="password" required placeholder="Confirm Password"
                    class="w-full h-[60px] px-4 py-3 bg-gray-100 rounded-full border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all" />
                  <p v-if="passwordMismatch" class="mt-2 text-sm text-red-500">
                    Passwords do not match
                  </p>
                </div>
              </div>

              <!-- Column 2: Profile Picture and Sign Up Button (Right - 1/3) -->
              <div class="md:col-span-1 flex flex-col items-center justify-start space-y-6">
                <!-- Profile Picture Upload -->
                <div class="relative">
                  <label for="profile-upload"
                    class="cursor-pointer flex flex-col items-center justify-center w-40 h-40 bg-gray-100 rounded-full border-2 border-gray-700 hover:bg-gray-200 transition-colors shadow-lg">
                    <input id="profile-upload" type="file" accept="image/*" @change="(e:any)=>{ const f=(e.target?.files?.[0]||null); profileFile = f; profilePreview = f ? objectUrl(f) : null; }"
                      class="hidden" />
                    <div class="relative flex flex-col items-center justify-center w-full h-full">
                      <svg v-if="!profilePreview" class="w-12 h-12 text-gray-700" fill="currentColor"
                        viewBox="0 0 24 24">
                        <path
                          d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
                      </svg>
                      <img v-else :src="profilePreview" alt="Profile preview"
                        class="w-28 h-28 rounded-full object-cover" />
                      <!-- Upload arrow icon at bottom right -->
                      <div
                        class="absolute bottom-0 right-0 w-6 h-6 bg-gray-700 rounded-full flex items-center justify-center">
                        <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M7 11l5-5m0 0l5 5m-5-5v12" />
                        </svg>
                      </div>
                    </div>
                  </label>
                </div>

                <!-- Sign Up Button -->
                <button type="submit" :disabled="isSubmitting || passwordMismatch" 
                class="w-[200px] h-[60px] px-8 py-3 bg-gray-300 text-gray-900 
                      text-2xl font-bold rounded-full shadow-lg 
                      hover:bg-gray-400 transition-colors duration-200 
                      disabled:opacity-50 disabled:cursor-not-allowed">
                  <span v-if="!isSubmitting">Sign Up</span>
                  <span v-else>Signing Up...</span>
                </button>
              </div>
            </div>

            <!-- Error Message -->
            <div v-if="errorMessage" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg">
              {{ errorMessage }}
            </div>

            <!-- Login Link (Full Width) -->
            <div class="text-gray-400 text-center">
              Already have an account?
              <router-link to="/login"
                class="font-bold text-white hover:text-orange-500 transition-colors underline ml-1">
                Log In
              </router-link>
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
import AuthService from '@/services/AuthService';
import apiClient from '@/services/apiClient';
import type { AxiosError } from 'axios';

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
function objectUrl(file: File): string {
  return URL.createObjectURL(file);
}
const isSubmitting = ref(false);
const errorMessage = ref('');

const passwordMismatch = computed(() => {
  return (
    form.value.confirmPassword !== '' &&
    form.value.password !== form.value.confirmPassword
  );
});

// inline handler is used in template; dedicated function not needed

async function handleRegister() {
  if (passwordMismatch.value) {
    errorMessage.value = 'Passwords do not match';
    return;
  }

  isSubmitting.value = true;
  errorMessage.value = '';

  try {
    let profileImagePath: string | undefined = undefined;
    if (profileFile.value) {
      const formData = new FormData();
      formData.append('file', profileFile.value);
      formData.append('folder', 'profile-images');
      const { data: uploadResp } = await apiClient.post<{ url: string }>(
        '/api/upload/image',
        formData
      );
      profileImagePath = uploadResp.url;
    }

    const { data } = await AuthService.register({
      name: form.value.name,
      surname: form.value.surname,
      email: form.value.email,
      password: form.value.password,
      profileImagePath,
    });

    localStorage.setItem('user', JSON.stringify(data.user));
    localStorage.setItem('accessToken', data.accessToken);
    localStorage.setItem('refreshToken', data.refreshToken);
    window.dispatchEvent(new Event('auth-changed'));

    await router.push('/');
  } catch (err) {
    const axiosError = err as AxiosError<{ error?: string }>;
    errorMessage.value = axiosError.response?.data?.error ?? 'Registration failed. Please try again.';
  } finally {
    isSubmitting.value = false;
  }
}
</script>

<style scoped>
/* Additional styles if needed */
.text-shadow-lg {
  text-shadow: 0 4px 12px rgba(0, 0, 0, 0.35);
}
</style>
