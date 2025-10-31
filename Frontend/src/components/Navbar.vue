<template>
  <nav class="sticky top-0 z-50 bg-orange-500 shadow-lg border-b border-orange-600">
    <div class="w-full px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between items-center h-20">
        <!-- Left side: Logo and Web Text -->
        <div class="flex items-center space-x-4">
          <!-- Web Logo and Text Container -->
          <div class="flex-shrink-0 relative">
            <!-- Logo and Text for Desktop (Click to go home) -->
            <router-link 
              to="/"
              class="hidden xl:flex items-center space-x-4 focus:outline-none hover:opacity-80 transition-opacity duration-200"
            >
              <!-- Logo -->
              <div class="w-12 h-12 rounded-lg flex items-center justify-center overflow-hidden">
                <img 
                  src="/logo.gif" 
                  alt="Daybreak News Logo" 
                  class="w-full h-full object-contain"
                />
              </div>
              <!-- Web Text -->
              <div class="text-2xl sm:text-4xl font-bold text-white">
                Daybreak News
              </div>
            </router-link>
            
            <!-- Logo and Text for Mobile/Tablet (Dropdown trigger) -->
            <button
              @click="toggleDropdown"
              class="xl:hidden flex items-center space-x-4 focus:outline-none"
            >
              <!-- Logo -->
              <div class="w-12 h-12 rounded-lg flex items-center justify-center overflow-hidden">
                <img 
                  src="/logo.gif" 
                  alt="Daybreak News Logo" 
                  class="w-full h-full object-contain"
                />
              </div>
              <!-- Web Text -->
              <div class="text-2xl sm:text-4xl font-bold text-white">
                Daybreak News
              </div>
            </button>
            <!-- Dropdown menu (mobile and tablet only) -->
            <transition name="slide">
              <div
                v-if="dropdownOpen"
                class="absolute left-0 mt-2 w-48 bg-white rounded-lg shadow-xl z-50 flex flex-col xl:hidden border border-gray-200 overflow-hidden"
              >
                <template v-if="isLoggedIn">
                  <router-link
                    to="/profile"
                    class="block w-full px-4 py-3 bg-orange-100 hover:bg-orange-200 transition-colors rounded-t-lg"
                    style="margin-left: -1px; margin-right: -1px; margin-top: -1px; width: calc(100% + 2px);"
                  >
                    <div class="flex items-center gap-3">
                      <img 
                        v-if="currentUser?.profileImagePath"
                        :src="currentUser.profileImagePath"
                        :alt="`${currentUser.username} profile`"
                        class="w-10 h-10 rounded-full object-cover border-2 border-orange-300"
                      />
                      <div 
                        v-else
                        class="w-10 h-10 rounded-full bg-orange-200 flex items-center justify-center border-2 border-orange-300"
                      >
                        <svg class="w-6 h-6 text-orange-600" fill="currentColor" viewBox="0 0 20 20">
                          <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
                        </svg>
                      </div>
                      <div class="flex-1 min-w-0">
                        <div class="text-xs uppercase tracking-wide text-orange-600 font-medium">
                          Signed in as
                        </div>
                        <span class="text-orange-900 font-semibold inline-flex items-center gap-2 truncate text-sm">
                          @ {{ currentUser?.username }}
                          <VerifiedBadge :verified="currentUser?.verified" size="xs" />
                        </span>
                      </div>
                    </div>
                  </router-link>
                  <router-link
                    to="/"
                    class="px-4 py-3 hover:bg-orange-50 text-orange-700 font-medium transition-colors duration-200"
                    @click="closeDropdown"
                  >
                    All
                  </router-link>
                  <router-link
                    to="/fact"
                    class="px-4 py-3 hover:bg-green-50 text-green-700 font-medium transition-colors duration-200"
                    @click="closeDropdown"
                  >
                    Fact
                  </router-link>
                  <router-link
                    to="/fake"
                    class="px-4 py-3 hover:bg-red-50 text-red-700 font-medium transition-colors duration-200"
                    @click="closeDropdown"
                  >
                    Fake
                  </router-link>
                  <router-link
                    v-if="isAdmin"
                    to="/admin"
                    class="px-4 py-3 hover:bg-indigo-50 text-indigo-700 font-medium transition-colors duration-200"
                    @click="closeDropdown"
                  >
                    Admin Dashboard
                  </router-link>
                </template>
                <div class="border-t border-gray-200 my-2"></div>
                <template v-if="!isLoggedIn">
                  <router-link
                    to="/login"
                    class="px-4 py-3 hover:bg-orange-50 text-orange-700 font-medium transition-colors duration-200"
                    @click="closeDropdown"
                  >
                    Log In
                  </router-link>
                  <router-link
                    to="/register"
                    class="px-4 py-3 hover:bg-orange-50 text-orange-700 font-medium transition-colors duration-200"
                    @click="closeDropdown"
                  >
                    Register
                  </router-link>
                </template>
                <button
                  v-else
                  @click="handleLogout"
                  class="text-left px-4 py-3 hover:bg-orange-50 text-red-600 font-medium transition-colors duration-200"
                >
                  Logout
                </button>
                <div class="border-t border-gray-200 my-2" v-if="isLoggedIn"></div>
                <!-- Pagination settings dropdown - direct display -->
                <div
                  v-if="isLoggedIn && ($route.path === '/' || $route.path === '/fact' || $route.path === '/fake')"
                  class="px-4 py-2"
                >
                  <div class="relative pagination-control">
                    <button
                      @click="togglePaginationDropdown"
                      class="flex items-center gap-2 px-4 py-2 bg-orange-100 hover:bg-orange-200 text-orange-700 rounded-lg border border-orange-300 transition-all duration-200 shadow-sm hover:shadow-md focus:outline-none focus:ring-2 focus:ring-orange-300 focus:ring-offset-2"
                    >
                      <span class="font-medium">{{ localItemsPerPage }} per page</span>
                      <svg
                        class="w-4 h-4 transition-transform duration-200"
                        :class="{ 'rotate-180': paginationDropdownOpen }"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                      </svg>
                    </button>
                    <div
                      v-if="paginationDropdownOpen"
                      class="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-orange-200 z-50"
                    >
                      <div class="py-1">
                        <button
                          v-for="option in pageSizeOptions"
                          :key="option"
                          @click="selectItemsPerPage(option)"
                          class="w-full text-left px-4 py-2 text-sm hover:bg-orange-50 transition-colors duration-150"
                          :class="{ 'bg-orange-100 text-orange-700 font-medium': option === localItemsPerPage }"
                        >
                          {{ option }} per page
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </transition>
          </div>
        </div>

        <!-- Right side: Navigation Links and Pagination Control (hidden on mobile and tablet) -->
        <div class="hidden xl:flex items-center space-x-4 xl:space-x-6">
          <!-- Navigation Links -->
          <div class="flex space-x-4" v-if="isLoggedIn">
            <router-link 
              to="/"
              :class="[
                'px-6 py-2 rounded-md text-lg font-medium transition-colors duration-200 w-20 text-center',
                $route.path === '/' 
                  ? 'bg-white text-orange-700' 
                  : 'text-white hover:text-orange-200 hover:bg-orange-600'
              ]"
            >
              All
            </router-link>
            <router-link 
              to="/fact"
              :class="[
                'px-6 py-2 rounded-md text-lg font-medium transition-colors duration-200 w-20 text-center',
                $route.path === '/fact' 
                  ? 'bg-white text-green-700' 
                  : 'text-white hover:text-orange-200 hover:bg-orange-600'
              ]"
            >
              Fact
            </router-link>
            <router-link 
              to="/fake"
              :class="[
                'px-6 py-2 rounded-md text-lg font-medium transition-colors duration-200 w-20 text-center',
                $route.path === '/fake' 
                  ? 'bg-white text-red-700' 
                  : 'text-white hover:text-orange-200 hover:bg-orange-600'
              ]"
            >
              Fake
            </router-link>
            <router-link
              v-if="isAdmin"
              to="/admin"
              :class="[
                'px-6 py-2 rounded-md text-lg font-medium transition-colors duration-200 w-36 text-center',
                $route.path === '/admin'
                  ? 'bg-white text-indigo-700'
                  : 'text-white hover:text-orange-200 hover:bg-orange-600'
              ]"
            >
              Dashboard
            </router-link>
          </div>
          <!-- Auth Links -->
          <div class="flex items-center space-x-3 ml-4">
            <template v-if="!isLoggedIn">
              <router-link 
                to="/login"
                :class="[
                  'px-4 py-2 rounded-md text-sm font-medium transition-colors duration-200',
                  $route.path === '/login' 
                    ? 'bg-white text-orange-700' 
                    : 'text-white hover:text-orange-200 hover:bg-orange-600'
                ]"
              >
                Log In
              </router-link>
              <router-link 
                to="/register"
                :class="[
                  'px-4 py-2 rounded-md text-sm font-medium transition-colors duration-200',
                  $route.path === '/register' 
                    ? 'bg-white text-orange-700' 
                    : 'bg-white/20 text-white hover:bg-white/30'
                ]"
              >
                Register
              </router-link>
            </template>
            <template v-else>
              <router-link
                to="/profile"
                class="inline-flex items-center gap-3 px-4 py-2 rounded-md bg-orange-600 hover:bg-orange-700 text-white font-semibold max-w-[200px] truncate transition-colors duration-200 cursor-pointer"
              >
                <img 
                  v-if="currentUser?.profileImagePath"
                  :src="currentUser.profileImagePath"
                  :alt="`${currentUser.username} profile`"
                  class="w-8 h-8 rounded-full object-cover border-2 border-white/50"
                />
                <div 
                  v-else
                  class="w-8 h-8 rounded-full bg-white/20 flex items-center justify-center border-2 border-white/50"
                >
                  <svg class="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
                  </svg>
                </div>
                <span class="inline-flex items-center gap-2 truncate">
                  @ {{ currentUser?.username }}
                  <VerifiedBadge :verified="currentUser?.verified" size="xs" />
                </span>
              </router-link>
              <button
                @click="handleLogout"
                class="px-4 py-2 rounded-md text-sm font-medium bg-white/20 text-white hover:bg-white/30 transition-colors duration-200"
              >
                Logout
              </button>
            </template>
          </div>
          <!-- Pagination settings dropdown - only show on home page and fact/fake pages -->
          <div
            v-if="isLoggedIn && ($route.path === '/' || $route.path === '/fact' || $route.path === '/fake')"
            class="relative pagination-control"
          >
            <button
              @click="togglePaginationDropdown"
              class="flex items-center gap-2 px-4 py-2 bg-orange-100 hover:bg-orange-200 text-orange-700 rounded-lg border border-orange-300 transition-all duration-200 shadow-sm hover:shadow-md focus:outline-none focus:ring-2 focus:ring-orange-300 focus:ring-offset-2"
            >
              <span class="font-medium">{{ localItemsPerPage }} per page</span>
              <svg
                class="w-4 h-4 transition-transform duration-200"
                :class="{ 'rotate-180': paginationDropdownOpen }"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
              </svg>
            </button>

            <div
              v-if="paginationDropdownOpen"
              class="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-orange-200 z-50"
            >
              <div class="py-1">
                <button
                  v-for="option in pageSizeOptions"
                  :key="option"
                  @click="selectItemsPerPage(option)"
                  class="w-full text-left px-4 py-2 text-sm hover:bg-orange-50 transition-colors duration-150"
                  :class="{ 'bg-orange-100 text-orange-700 font-medium': option === localItemsPerPage }"
                >
                  {{ option }} per page
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, onUnmounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import type { User } from '@/types'
import VerifiedBadge from '@/components/VerifiedBadge.vue'

interface Props {
  itemsPerPage: number
}

const props = defineProps<Props>()
const emit = defineEmits<{
  'update:itemsPerPage': [value: number]
}>()
const router = useRouter()
const localItemsPerPage = ref(props.itemsPerPage)
const pageSizeOptions = [3, 6, 9, 12, 15]
const paginationDropdownOpen = ref(false)

// Responsive dropdown states
const dropdownOpen = ref(false)

function toggleDropdown() {
  dropdownOpen.value = !dropdownOpen.value
  if (!dropdownOpen.value) {
    closePaginationDropdown()
  }
}

function togglePaginationDropdown() {
  paginationDropdownOpen.value = !paginationDropdownOpen.value
}

function closePaginationDropdown() {
  paginationDropdownOpen.value = false
}

function closeDropdown() {
  dropdownOpen.value = false
  closePaginationDropdown()
}

// Close dropdown when clicking outside
function handleClickOutside(event: Event) {
  const target = event.target as HTMLElement
  const insideMainTrigger = target.closest('.flex-shrink-0')
  const insidePaginationControl = target.closest('.pagination-control')

  if (!insideMainTrigger && !insidePaginationControl) {
    closeDropdown()
  }
  if (!insidePaginationControl) {
    closePaginationDropdown()
  }
}

// Close dropdown on escape key
function handleEscape(event: KeyboardEvent) {
  if (event.key === 'Escape') {
    closeDropdown()
  }
}

// Watch for external changes to itemsPerPage prop
watch(() => props.itemsPerPage, (newValue) => {
  localItemsPerPage.value = newValue
})

function selectItemsPerPage(value: number) {
  updateItemsPerPage(value)
  closePaginationDropdown()
}

function updateItemsPerPage(value: number) {
  localItemsPerPage.value = value
  emit('update:itemsPerPage', value)
}

// Close dropdown on route change
const route = useRoute()
watch(() => route.path, closeDropdown)

// Add event listeners
// Auth state and logout handler
const isLoggedIn = ref<boolean>(false)
const currentUser = ref<User | null>(null)
const isAdmin = computed(() => {
  const roles = currentUser.value?.roles
  return Array.isArray(roles) ? roles.includes('ADMIN') : false
})

function refreshAuthState() {
  const rawUser = localStorage.getItem('user')
  if (!rawUser) {
    isLoggedIn.value = false
    currentUser.value = null
    return
  }
  isLoggedIn.value = true
  try {
    currentUser.value = JSON.parse(rawUser) as User
  } catch (error) {
    console.warn('Unable to parse stored user', error)
    currentUser.value = null
  }
}

function handleLogout() {
  localStorage.removeItem('user')
  localStorage.removeItem('accessToken')
  localStorage.removeItem('refreshToken')
  currentUser.value = null
  refreshAuthState()
  closeDropdown()
  router.push('/login')
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  document.addEventListener('keydown', handleEscape)
  refreshAuthState()
  window.addEventListener('storage', refreshAuthState)
  window.addEventListener('auth-changed', refreshAuthState as EventListener)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  document.removeEventListener('keydown', handleEscape)
  window.removeEventListener('storage', refreshAuthState)
  window.removeEventListener('auth-changed', refreshAuthState as EventListener)
})
</script>

