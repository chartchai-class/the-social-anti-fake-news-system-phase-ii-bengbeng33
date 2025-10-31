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
          <h1 class="text-4xl sm:text-5xl font-bold text-black mb-8 
           drop-shadow-[2px_2px_3px_rgba(255,255,255,0.8)] text-center">
            Login
          </h1>

          
          <form class="space-y-6" @submit.prevent="onSubmit">
            <!-- Email -->
            <div class="flex flex-col items-center gap-2">
              <input
                v-model="email"
                type="email"
                placeholder="Email Address"
                @blur="handleEmailBlur"
                class="w-[500px] h-[60px] px-4 py-3 bg-gray-100 rounded-full border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all"
              />
              <p v-if="errors.email" class="text-sm text-red-600">{{ errors.email }}</p>
            </div>

            <!-- Password -->
            <div class="flex flex-col items-center gap-2">
              <input
                v-model="password"
                type="password"
                placeholder="Password"
                class="w-[500px] h-[60px] px-4 py-3 bg-gray-100 rounded-full border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-all"
              />
              <p v-if="errors.password" class="text-sm text-red-600">{{ errors.password }}</p>
            </div>
  
            <!-- Sign In Button -->
            <div class="flex justify-center">
              <button
                type="submit"
                :disabled="isSubmitting"
                class="w-[250px] h-[60px] px-8 py-3 text-2xl bg-gray-300 text-gray-900 rounded-full font-bold shadow-lg hover:bg-gray-400 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
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
            <div class="text-gray-400 text-center">
              Do not have an account?
              <router-link
                to="/register"
                class="font-bold text-white hover:text-orange-500 transition-colors underline ml-1"
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
import { useRouter, useRoute } from 'vue-router';
import AuthService from '@/services/AuthService';
import type { AxiosError } from 'axios';
import * as yup from 'yup';
import { useForm, useField } from 'vee-validate';

const router = useRouter();
const route = useRoute();

const validationSchema = yup.object({
  email: yup.string().required('The email is required').email('Invalid email address'),
  password: yup.string().required('The password is required')
});

const { errors, handleSubmit } = useForm({
  validationSchema,
  initialValues: { email: '', password: '' },
});

const { value: email, handleBlur: handleEmailBlur } = useField<string>('email');
const { value: password } = useField<string>('password');

const isSubmitting = ref(false);
const errorMessage = ref('');

const onSubmit = handleSubmit(async (values) => {
  isSubmitting.value = true;
  errorMessage.value = '';
  try {
    const { data } = await AuthService.login({
      email: values.email,
      password: values.password,
    });

    localStorage.setItem('user', JSON.stringify(data.user));
    localStorage.setItem('accessToken', data.accessToken);
    localStorage.setItem('refreshToken', data.refreshToken);
    window.dispatchEvent(new Event('auth-changed'));

    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/';
    await router.push(redirect);
  } catch (err) {
    const axiosError = err as AxiosError<{ error?: string }>;
    errorMessage.value = axiosError.response?.data?.error ?? 'Login failed. Please check your credentials and try again.';
  } finally {
    isSubmitting.value = false;
  }
});
</script>

<style scoped>
/* Additional styles if needed */
.text-shadow-lg {
  text-shadow: 0 4px 12px rgba(0, 0, 0, 0.35);
}
</style>
  
