<template>
  <div>
    <!-- Background Effects -->
    <div class="absolute inset-0 overflow-hidden">
      <!-- Reddish-orange glow at top right -->
      <div class="absolute top-0 right-0 w-96 h-96 bg-orange-500/30 rounded-full blur-3xl"></div>
      <!-- Purplish-pink glow at bottom left -->
      <div class="absolute bottom-0 left-0 w-96 h-96 bg-pink-500/30 rounded-full blur-3xl"></div>
    </div>

    <!-- Login Form Container -->
    <div class="relative z-10 flex items-center justify-center min-h-screen py-12 px-4 sm:px-6 lg:px-8">
      <div class="w-full max-w-4xl mx-auto">
        <!-- Frosted Glass Form (Glassmorphism) -->
        <div class="bg-gray-100/20 backdrop-blur-md rounded-3xl shadow-2xl border border-gray-300/50 p-8 sm:p-12">
          <h1 class="text-4xl sm:text-5xl font-bold text-black drop-shadow-lg drop-shadow-white mb-8">Login</h1>
          
          <form @submit.prevent="handleLogin" class="space-y-6">
            <!-- Email -->
            <div>
              <input
                v-model="form.email"
                type="email"
                required
                placeholder="Email Address"
                class="w-full h-20 px-4 py-3 bg-gray-100 rounded-full border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all"
              />
            </div>

            <!-- Password -->
            <div>
              <input
                v-model="form.password"
                type="password"
                required
                placeholder="Password"
                class="w-full h-20 px-4 py-3 bg-gray-100 rounded-full border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all"
              />
            </div>

            <!-- Sign In Button -->
            <div class="flex justify-center">
              <button
                type="submit"
                :disabled="isSubmitting"
                class="w-48 h-20 px-8 py-3 bg-gray-300 text-gray-900 rounded-full font-bold shadow-lg hover:bg-gray-400 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <span v-if="!isSubmitting">Log In</span>
                <span v-else>Logging In...</span>
              </button>
            </div>

            <!-- Error Message -->
            <div v-if="errorMessage" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg">
              {{ errorMessage }}
            </div>

            <!-- Register Link (Full Width) -->
            <div class="text-white text-center">
              Do not have an account?
              <router-link
                to="/register"
                class="font-bold text-gray-900 hover:text-orange-500 transition-colors underline ml-1"
              >
                Register
              </router-link>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const form = ref({
  email: '',
  password: '',
});

const isSubmitting = ref(false);
const errorMessage = ref('');

async function handleLogin() {
  isSubmitting.value = true;
  errorMessage.value = '';

  try {
    // TODO: Implement actual login API call
    // For now, just simulate a delay and redirect
    await new Promise((resolve) => setTimeout(resolve, 1000));
    
    // Store user info (you can replace this with actual auth logic)
    localStorage.setItem('user', JSON.stringify({
      email: form.value.email,
    }));

    // Redirect to home page
    router.push('/');
  } catch {
    errorMessage.value = 'Login failed. Please check your credentials and try again.';
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
  