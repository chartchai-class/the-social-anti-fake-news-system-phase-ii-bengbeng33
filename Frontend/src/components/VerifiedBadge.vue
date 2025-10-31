<template>
  <span
    v-if="verified"
    class="inline-flex items-center justify-center rounded-full bg-green-500 text-white font-semibold"
    :class="sizeClasses"
    :title="title"
  >
    ✓
  </span>
</template>

<script setup lang="ts">
import { computed } from "vue";

const props = withDefaults(
  defineProps<{
    verified?: boolean | null;
    size?: "xs" | "sm" | "md";
    title?: string;
  }>(),
  {
    size: "sm",
    title: "Verified user",
  }
);

const sizeClasses = computed(() => {
  switch (props.size) {
    case "xs":
      return "h-4 w-4 text-[10px]";
    case "md":
      return "h-6 w-6 text-sm";
    default:
      return "h-5 w-5 text-xs";
  }
});
</script>
